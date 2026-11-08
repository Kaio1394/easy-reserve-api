package br.com.easyreserve.features.environment.service;

import br.com.easyreserve.exception.EnvironmentAlreadyExistsException;
import br.com.easyreserve.features.environment.dto.*;
import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.environment.repository.EnvironmentRepository;
import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;
import br.com.easyreserve.features.environment_resources.repository.EnvironmentResourceRepository;
import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.key.repository.KeyRepository;
import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.reservation.repository.ReservationRepository;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final WorkspaceRepository workspaceRepository;
    private final EnvironmentResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;
    private final KeyRepository keyRepository;

    public EnvironmentServiceImpl(EnvironmentRepository environmentRepository,
                                  WorkspaceRepository workspaceRepository,
                                  EnvironmentResourceRepository resourceRepository,
                                  ReservationRepository reservationRepository,
                                  KeyRepository keyRepository) {
        this.environmentRepository = environmentRepository;
        this.workspaceRepository = workspaceRepository;
        this.resourceRepository = resourceRepository;
        this.reservationRepository = reservationRepository;
        this.keyRepository = keyRepository;
    }

    @Transactional
    @Override
    public EnvironmentGetDto add(EnvironmentCreateDto dto) {
        WorkspaceEntity workspace = workspaceRepository.findById(dto.workspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        if (environmentRepository.findByWorkspaceIdAndName(dto.workspaceId(), dto.name()).isPresent()) {
            throw new EnvironmentAlreadyExistsException(workspace.getName(), dto.name());
        }

        EnvironmentEntity entity = dto.toEntity(workspace);
        entity = environmentRepository.save(entity);
        return EnvironmentGetDto.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnvironmentGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        List<EnvironmentEntity> envs = wsId != null
                ? environmentRepository.findByWorkspaceId(wsId)
                : environmentRepository.findAllWithWorkspace();
        return envs.stream()
                .filter(e -> Boolean.TRUE.equals(e.getActive()))
                .map(EnvironmentGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void delete(EnvironmentDeleteDto dto) {
        EnvironmentEntity entity = environmentRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));
        // Soft Delete: desativa o ambiente preservando integridade das reservas e histórico
        entity.setActive(Boolean.FALSE);
        environmentRepository.save(entity);
    }

    @Transactional
    @Override
    public EnvironmentGetDto update(EnvironmentUpdateDto dto) {
        EnvironmentEntity entity = environmentRepository.findByIdWithWorkspace(dto.id())
                .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));

        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
        if (dto.capacity() != null) {
            entity.setCapacity(dto.capacity());
        }
        if (dto.active() != null) {
            entity.setActive(dto.active());
        }

        entity = environmentRepository.save(entity);
        return EnvironmentGetDto.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnvironmentRecommendationItemDto> recommend(EnvironmentRecommendationRequestDto dto) {
        WorkspaceEntity workspace = workspaceRepository.findById(dto.workspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        List<EnvironmentEntity> envList = environmentRepository.findAllWithWorkspace()
                .stream()
                .filter(e -> e.getWorkspace().getId().equals(workspace.getId()) && Boolean.TRUE.equals(e.getActive()))
                .toList();

        List<EnvironmentResourceEntity> allResources = resourceRepository.findAll()
                .stream()
                .filter(r -> r.getWorkspace() != null
                        && r.getWorkspace().getId().equals(workspace.getId())
                        && Boolean.TRUE.equals(r.getActive()))
                .toList();

        // Mapa de pesos definidos pelo usuário diretamente no formulário do assistente
        Map<String, Double> userWeights = new HashMap<>();
        if (dto.resourceWeights() != null) {
            dto.resourceWeights().forEach((k, v) -> {
                if (k != null && v != null) {
                    userWeights.put(k.trim().toLowerCase(), v);
                }
            });
        }

        List<ReservationEntity> activeReservations = reservationRepository.findAllWithDetails()
                .stream()
                .filter(r -> r.getWorkspace().getId().equals(workspace.getId())
                        && r.getStatus() == ReservationStatus.ACTIVE
                        && Boolean.TRUE.equals(r.getActive()))
                .toList();

        List<String> reqList = dto.requiredResources() != null
                ? dto.requiredResources().stream().map(String::trim).filter(s -> !s.isEmpty()).toList()
                : List.of();

        double totalRequiredWeight = 0.0;
        for (String req : reqList) {
            totalRequiredWeight += userWeights.getOrDefault(req.toLowerCase(), 1.0);
        }

        List<EnvironmentRecommendationItemDto> recommendations = new ArrayList<>();

        LocalDateTime targetStart = dto.startTime() != null ? dto.startTime() : LocalDateTime.now();
        LocalDateTime targetEnd = dto.endTime() != null ? dto.endTime() : targetStart.plusHours(1);

        for (EnvironmentEntity env : envList) {
            // Verifica conflito de reserva no horário solicitado
            boolean hasOverlap = activeReservations.stream()
                    .filter(r -> r.getEnvironment().getId().equals(env.getId()))
                    .anyMatch(r -> hasTimeOverlap(targetStart, targetEnd, r.getStartTime(), r.getEndTime()));

            List<KeyEntity> keys = keyRepository.findByEnvironmentId(env.getId());
            LocalDateTime now = LocalDateTime.now();
            boolean isRoomInUseNow = activeReservations.stream()
                    .filter(r -> r.getEnvironment().getId().equals(env.getId()))
                    .anyMatch(r -> {
                        LocalDateTime start = r.getStartTime() != null ? r.getStartTime() : now;
                        LocalDateTime end = r.getEndTime() != null ? r.getEndTime() : start.plusHours(1);
                        return !now.isBefore(start) && now.isBefore(end);
                    });

            boolean available = !hasOverlap;

            String keyStatusStr;
            if (keys.isEmpty()) {
                keyStatusStr = "Sem chaves cadastradas";
            } else if (!isRoomInUseNow) {
                keyStatusStr = "Chave disponível na portaria";
            } else {
                keyStatusStr = "Chave em uso no momento (disponível em outros horários)";
            }

            // Recursos operacionais deste ambiente
            List<EnvironmentResourceEntity> roomResources = allResources.stream()
                    .filter(r -> r.getEnvironment() != null
                            && r.getEnvironment().getId().equals(env.getId())
                            && Boolean.TRUE.equals(r.getOperational()))
                    .toList();

            Map<String, Integer> roomResMap = new HashMap<>();
            roomResources.forEach(r -> roomResMap.put(r.getName().trim().toLowerCase(), r.getQuantity() != null ? r.getQuantity() : 1));

            double matchedWeight = 0.0;
            List<String> matched = new ArrayList<>();
            List<String> missing = new ArrayList<>();

            if (reqList.isEmpty()) {
                // Se o usuário não especificou recursos pontuais, soma o peso de todos os itens operacionais presentes
                for (EnvironmentResourceEntity rr : roomResources) {
                    double w = userWeights.getOrDefault(rr.getName().trim().toLowerCase(), 1.0);
                    matchedWeight += w;
                    matched.add(rr.getName() + " (" + (rr.getQuantity() != null ? rr.getQuantity() : 1) + "x)");
                }
            } else {
                for (String req : reqList) {
                    double w = userWeights.getOrDefault(req.toLowerCase(), 1.0);
                    String foundKey = null;
                    int qty = 0;
                    for (Map.Entry<String, Integer> entry : roomResMap.entrySet()) {
                        if (entry.getKey().contains(req.toLowerCase()) || req.toLowerCase().contains(entry.getKey())) {
                            foundKey = entry.getKey();
                            qty = entry.getValue();
                            break;
                        }
                    }
                    if (foundKey != null) {
                        matchedWeight += w;
                        matched.add(req + " (" + qty + "x)");
                    } else {
                        missing.add(req);
                    }
                }
            }

            int matchPercentage;
            if (reqList.isEmpty()) {
                matchPercentage = roomResources.isEmpty() ? 60 : 100;
            } else if (totalRequiredWeight > 0) {
                matchPercentage = (int) Math.round((matchedWeight / totalRequiredWeight) * 100.0);
            } else {
                matchPercentage = 100;
            }

            boolean meetsCapacity = true;
            if (dto.minCapacity() != null && dto.minCapacity() > 0) {
                if (env.getCapacity() == null || env.getCapacity() < dto.minCapacity()) {
                    meetsCapacity = false;
                    matchPercentage = Math.max(0, matchPercentage - 20);
                }
            }

            double matchScore = matchedWeight + (available ? 10.0 : 0.0) + (meetsCapacity ? 5.0 : 0.0);

            StringBuilder reason = new StringBuilder();
            if (available) {
                reason.append("Ambiente disponível no horário. ");
            } else if (hasOverlap) {
                reason.append("Ambiente já reservado neste horário. ");
            } else {
                reason.append("Chave atualmente em uso. ");
            }

            if (!matched.isEmpty()) {
                reason.append("Atende recursos solicitados (peso ponderado). ");
            }
            if (!missing.isEmpty()) {
                reason.append("Faltam: ").append(String.join(", ", missing)).append(". ");
            }
            if (dto.minCapacity() != null && dto.minCapacity() > 0) {
                if (meetsCapacity) {
                    reason.append("Capacidade atende à solicitação (").append(env.getCapacity() != null ? env.getCapacity() : 0).append(" lugares).");
                } else {
                    reason.append("Capacidade inferior à desejada (").append(env.getCapacity() != null ? env.getCapacity() : 0).append(" lugares).");
                }
            }

            recommendations.add(new EnvironmentRecommendationItemDto(
                    env.getId(),
                    env.getName(),
                    env.getDescription(),
                    env.getCapacity(),
                    matchScore,
                    matchPercentage,
                    matched,
                    missing,
                    available,
                    keyStatusStr,
                    reason.toString().trim()
            ));
        }

        // Ordenação inteligente:
        // 1. Ambientes disponíveis primeiro
        // 2. Maior compatibilidade percentual
        // 3. Maior pontuação ponderada (matchScore)
        // 4. Nome
        return recommendations.stream()
                .sorted(Comparator.comparing(EnvironmentRecommendationItemDto::available).reversed()
                        .thenComparing(Comparator.comparing(EnvironmentRecommendationItemDto::matchPercentage).reversed())
                        .thenComparing(Comparator.comparing(EnvironmentRecommendationItemDto::matchScore).reversed())
                        .thenComparing(EnvironmentRecommendationItemDto::environmentName))
                .toList();
    }

    private boolean hasTimeOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        LocalDateTime effectiveStart1 = start1 != null ? start1 : LocalDateTime.now();
        LocalDateTime effectiveEnd1 = end1 != null ? end1 : effectiveStart1.plusHours(1);
        LocalDateTime effectiveStart2 = start2 != null ? start2 : LocalDateTime.now();
        LocalDateTime effectiveEnd2 = end2 != null ? end2 : effectiveStart2.plusHours(1);

        return effectiveStart1.isBefore(effectiveEnd2) && effectiveStart2.isBefore(effectiveEnd1);
    }
}
