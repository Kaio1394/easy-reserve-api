package br.com.easyreserve.features.reservation.service;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.environment.repository.EnvironmentRepository;
import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.key.repository.KeyRepository;
import br.com.easyreserve.features.reservation.dto.ReservationCreateDto;
import br.com.easyreserve.features.reservation.dto.ReservationDeleteDto;
import br.com.easyreserve.features.reservation.dto.ReservationGetDto;
import br.com.easyreserve.features.reservation.dto.ReservationUpdateDto;
import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.reservation.repository.ReservationRepository;
import br.com.easyreserve.features.reservation_history.enums.ReservationHistoryAction;
import br.com.easyreserve.features.reservation_history.service.ReservationHistoryService;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.user.repository.UserRepository;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final EnvironmentRepository environmentRepository;
    private final KeyRepository keyRepository;
    private final UserRepository userRepository;
    private final ReservationHistoryService reservationHistoryService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  WorkspaceRepository workspaceRepository,
                                  EnvironmentRepository environmentRepository,
                                  KeyRepository keyRepository,
                                  UserRepository userRepository,
                                  ReservationHistoryService reservationHistoryService) {
        this.reservationRepository = reservationRepository;
        this.workspaceRepository = workspaceRepository;
        this.environmentRepository = environmentRepository;
        this.keyRepository = keyRepository;
        this.userRepository = userRepository;
        this.reservationHistoryService = reservationHistoryService;
    }

    @Transactional
    @Override
    public ReservationGetDto add(ReservationCreateDto dto) {
        WorkspaceEntity workspace = workspaceRepository.findById(dto.workspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        EnvironmentEntity environment = environmentRepository.findById(dto.environmentId())
                .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));

        LocalDateTime newStart = dto.startTime() != null ? dto.startTime() : LocalDateTime.now();
        LocalDateTime newEnd = dto.endTime() != null ? dto.endTime() : newStart.plusHours(1);

        if (dto.endTime() != null && !dto.endTime().isAfter(newStart)) {
            throw new RuntimeException("O horário de término deve ser posterior ao horário de início da reserva");
        }

        List<ReservationEntity> activeEnvReservations = reservationRepository.findActiveByEnvironmentId(
                environment.getId(),
                ReservationStatus.ACTIVE
        );
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (ReservationEntity existing : activeEnvReservations) {
            if (hasTimeOverlap(newStart, newEnd, existing.getStartTime(), existing.getEndTime())) {
                String periodStr = existing.getStartTime().format(fmt) + " às " +
                        (existing.getEndTime() != null ? existing.getEndTime().format(fmt) : "em aberto");
                throw new RuntimeException(
                        "O ambiente \"" + environment.getName() + "\" já está reservado neste horário ("
                                + periodStr + ") por " + existing.getUser().getName()
                                + ". Não é permitido reservar o mesmo ambiente no mesmo horário."
                );
            }
        }

        KeyEntity key = keyRepository.findById(dto.keyId())
                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));

        if (!key.getEnvironment().getId().equals(environment.getId())) {
            throw new RuntimeException("A chave selecionada não pertence ao ambiente informado");
        }

        // Verifica se esta chave específica já está reservada para o mesmo horário solicitado
        for (ReservationEntity existing : activeEnvReservations) {
            if (existing.getKey() != null && existing.getKey().getId().equals(key.getId())
                    && hasTimeOverlap(newStart, newEnd, existing.getStartTime(), existing.getEndTime())) {
                String periodStr = existing.getStartTime().format(fmt) + " às " +
                        (existing.getEndTime() != null ? existing.getEndTime().format(fmt) : "em aberto");
                throw new RuntimeException(
                        "A chave \"" + key.getCode() + "\" já está reservada neste horário ("
                                + periodStr + ") por " + existing.getUser().getName()
                                + ". Não é permitido reservar a mesma chave no mesmo horário."
                );
            }
        }

        UserEntity user;
        if (dto.userId() != null) {
            user = userRepository.findById(dto.userId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserEntity authUser) {
                user = authUser;
            } else {
                throw new RuntimeException("Usuário responsável pela reserva não identificado");
            }
        }

        // A chave passa para o status EM USO (available = false) SOMENTE se a reserva está ocorrendo neste momento exato
        LocalDateTime now = LocalDateTime.now();
        boolean isOngoingNow = !now.isBefore(newStart) && now.isBefore(newEnd);
        if (isOngoingNow) {
            key.setAvailable(Boolean.FALSE);
            keyRepository.save(key);
        } else {
            // Se a reserva é para outro horário (ex: futuro), a chave só estará EM USO se houver outra reserva ocorrendo agora
            boolean otherOngoingNow = activeEnvReservations.stream()
                    .filter(r -> r.getKey() != null && r.getKey().getId().equals(key.getId()))
                    .anyMatch(r -> {
                        LocalDateTime start = r.getStartTime() != null ? r.getStartTime() : now;
                        LocalDateTime end = r.getEndTime() != null ? r.getEndTime() : start.plusHours(1);
                        return !now.isBefore(start) && now.isBefore(end);
                    });
            key.setAvailable(!otherOngoingNow);
            keyRepository.save(key);
        }

        ReservationEntity entity = dto.toEntity(workspace, environment, key, user);
        entity = reservationRepository.save(entity);

        String historyNotes = isOngoingNow
                ? "Reserva confirmada — chave " + key.getCode() + " retirada (EM USO no momento)"
                : "Reserva agendada — chave " + key.getCode() + " permanece DISPONÍVEL na portaria até o horário agendado";

        reservationHistoryService.recordHistory(
                entity,
                ReservationHistoryAction.CREATED,
                historyNotes,
                false
        );

        return ReservationGetDto.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        List<ReservationEntity> reservations = wsId != null
                ? reservationRepository.findByWorkspaceId(wsId)
                : reservationRepository.findAllWithDetails();
        return reservations.stream()
                .filter(r -> Boolean.TRUE.equals(r.getActive()))
                .map(ReservationGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public ReservationGetDto update(ReservationUpdateDto dto) {
        ReservationEntity entity = reservationRepository.findByIdWithDetails(dto.id())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (dto.purpose() != null) {
            entity.setPurpose(dto.purpose());
        }
        if (dto.startTime() != null) {
            entity.setStartTime(dto.startTime());
        }
        if (dto.endTime() != null) {
            entity.setEndTime(dto.endTime());
        }

        ReservationHistoryAction historyAction = ReservationHistoryAction.UPDATED;
        String historyNotes = "Dados da reserva atualizados";

        if (dto.status() != null && dto.status() != entity.getStatus()) {
            entity.setStatus(dto.status());
            KeyEntity key = entity.getKey();
            if (dto.status() == ReservationStatus.COMPLETED || dto.status() == ReservationStatus.CANCELLED) {
                if (entity.getEndTime() == null) {
                    entity.setEndTime(LocalDateTime.now());
                }
                syncKeyAvailabilityAtNow(key, dto.id());

                if (dto.status() == ReservationStatus.COMPLETED) {
                    historyAction = ReservationHistoryAction.COMPLETED;
                    historyNotes = "Reserva encerrada — chave " + key.getCode() + " devolvida à portaria";
                } else {
                    historyAction = ReservationHistoryAction.CANCELLED;
                    historyNotes = "Reserva cancelada — chave " + key.getCode() + " liberada";
                }
            } else if (dto.status() == ReservationStatus.ACTIVE) {
                LocalDateTime checkStart = entity.getStartTime() != null ? entity.getStartTime() : LocalDateTime.now();
                LocalDateTime checkEnd = entity.getEndTime() != null ? entity.getEndTime() : checkStart.plusHours(1);
                List<ReservationEntity> otherActiveEnvReservations = reservationRepository.findActiveByEnvironmentId(
                        entity.getEnvironment().getId(),
                        ReservationStatus.ACTIVE
                ).stream().filter(r -> !r.getId().equals(dto.id())).toList();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (ReservationEntity existing : otherActiveEnvReservations) {
                    if (hasTimeOverlap(checkStart, checkEnd, existing.getStartTime(), existing.getEndTime())) {
                        String periodStr = existing.getStartTime().format(fmt) + " às " +
                                (existing.getEndTime() != null ? existing.getEndTime().format(fmt) : "em aberto");
                        throw new RuntimeException(
                                "O ambiente \"" + entity.getEnvironment().getName() + "\" já está reservado neste horário ("
                                        + periodStr + ") por " + existing.getUser().getName() + "."
                        );
                    }
                }
                syncKeyAvailabilityAtNow(key, null);
                historyAction = ReservationHistoryAction.UPDATED;
                historyNotes = "Reserva reativada — chave " + key.getCode() + " verificada";
            }
        }

        if (dto.active() != null) {
            entity.setActive(dto.active());
        }

        entity = reservationRepository.save(entity);

        reservationHistoryService.recordHistory(entity, historyAction, historyNotes, false);

        return ReservationGetDto.fromEntity(entity);
    }

    @Transactional
    @Override
    public void delete(ReservationDeleteDto dto) {
        ReservationEntity entity = reservationRepository.findByIdWithDetails(dto.id())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // Se a reserva estava ativa ao ser excluída, recalcula a disponibilidade da chave para o momento atual
        if (entity.getKey() != null) {
            syncKeyAvailabilityAtNow(entity.getKey(), dto.id());
        }

        String keyCode = entity.getKey() != null ? entity.getKey().getCode() : "";
        reservationHistoryService.recordHistory(
                entity,
                ReservationHistoryAction.DELETED,
                "Reserva excluída — chave " + keyCode + " liberada",
                true
        );

        reservationRepository.delete(entity);
    }

    private void syncKeyAvailabilityAtNow(KeyEntity key, java.util.UUID ignoreReservationId) {
        if (key == null || key.getId() == null) return;
        LocalDateTime now = LocalDateTime.now();
        List<ReservationEntity> activeEnvReservations = reservationRepository.findActiveByEnvironmentId(
                key.getEnvironment().getId(),
                ReservationStatus.ACTIVE
        );
        boolean inUseNow = activeEnvReservations.stream()
                .filter(r -> (ignoreReservationId == null || !r.getId().equals(ignoreReservationId))
                        && r.getKey() != null
                        && r.getKey().getId().equals(key.getId()))
                .anyMatch(r -> {
                    LocalDateTime start = r.getStartTime() != null ? r.getStartTime() : now;
                    LocalDateTime end = r.getEndTime() != null ? r.getEndTime() : start.plusHours(1);
                    return !now.isBefore(start) && now.isBefore(end);
                });
        key.setAvailable(!inUseNow);
        keyRepository.save(key);
    }

    private boolean hasTimeOverlap(
            LocalDateTime start1,
            LocalDateTime end1,
            LocalDateTime start2,
            LocalDateTime end2
    ) {
        LocalDateTime effectiveStart1 = start1 != null ? start1 : LocalDateTime.now();
        LocalDateTime effectiveEnd1 = end1 != null ? end1 : effectiveStart1.plusHours(1);
        LocalDateTime effectiveStart2 = start2 != null ? start2 : LocalDateTime.now();
        LocalDateTime effectiveEnd2 = end2 != null ? end2 : effectiveStart2.plusHours(1);

        return effectiveStart1.isBefore(effectiveEnd2) && effectiveStart2.isBefore(effectiveEnd1);
    }
}
