package br.com.easyreserve.features.key.service;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.environment.repository.EnvironmentRepository;
import br.com.easyreserve.features.key.dto.KeyCreateDto;
import br.com.easyreserve.features.key.dto.KeyDeleteDto;
import br.com.easyreserve.features.key.dto.KeyGetDto;
import br.com.easyreserve.features.key.dto.KeyUpdateDto;
import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.key.repository.KeyRepository;
import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KeyServiceImpl implements KeyService {

    private final KeyRepository keyRepository;
    private final EnvironmentRepository environmentRepository;
    private final ReservationRepository reservationRepository;

    public KeyServiceImpl(KeyRepository keyRepository,
                          EnvironmentRepository environmentRepository,
                          ReservationRepository reservationRepository) {
        this.keyRepository = keyRepository;
        this.environmentRepository = environmentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Override
    public KeyGetDto add(KeyCreateDto dto) {
        EnvironmentEntity environment = environmentRepository.findById(dto.environmentId())
                .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));

        java.util.Optional<KeyEntity> existingKeyOpt = keyRepository.findByEnvironmentIdAndCode(dto.environmentId(), dto.code());
        if (existingKeyOpt.isPresent()) {
            KeyEntity existing = existingKeyOpt.get();
            if (Boolean.TRUE.equals(existing.getActive())) {
                throw new RuntimeException("Já existe uma chave com este código neste ambiente");
            } else {
                // Se a chave existia mas estava inativa (excluída logicamente), reativa com status disponível
                existing.setCode(dto.code().trim());
                existing.setActive(Boolean.TRUE);
                existing.setAvailable(Boolean.TRUE);
                keyRepository.save(existing);
                return KeyGetDto.fromEntity(existing);
            }
        }

        KeyEntity entity = dto.toEntity(environment);
        entity = keyRepository.save(entity);
        return KeyGetDto.fromEntity(entity);
    }

    @Transactional
    @Override
    public List<KeyGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        LocalDateTime now = LocalDateTime.now();
        List<ReservationEntity> allReservations = wsId != null
                ? reservationRepository.findByWorkspaceId(wsId)
                : reservationRepository.findAllWithDetails();

        List<KeyEntity> keys = wsId != null
                ? keyRepository.findByWorkspaceId(wsId)
                : keyRepository.findAllWithDetails();

        for (KeyEntity key : keys) {
            boolean inUseNow = allReservations.stream()
                    .filter(r -> Boolean.TRUE.equals(r.getActive())
                            && r.getStatus() == ReservationStatus.ACTIVE
                            && r.getKey() != null
                            && r.getKey().getId().equals(key.getId()))
                    .anyMatch(r -> {
                        LocalDateTime start = r.getStartTime() != null ? r.getStartTime() : now;
                        LocalDateTime end = r.getEndTime() != null ? r.getEndTime() : start.plusHours(1);
                        return !now.isBefore(start) && now.isBefore(end);
                    });

            boolean expectedAvailable = !inUseNow;
            if (expectedAvailable != Boolean.TRUE.equals(key.getAvailable())) {
                key.setAvailable(expectedAvailable);
                keyRepository.save(key);
            }
        }

        return keys.stream().map(KeyGetDto::fromEntity).toList();
    }

    @Transactional
    @Override
    public void delete(KeyDeleteDto dto) {
        KeyEntity entity = keyRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));

        // Exclusão lógica (Soft Delete): desativa a chave sem corromper o histórico de auditoria
        entity.setActive(Boolean.FALSE);
        entity.setAvailable(Boolean.FALSE);
        keyRepository.save(entity);
    }

    @Transactional
    @Override
    public KeyGetDto update(KeyUpdateDto dto) {
        KeyEntity entity = keyRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));

        if (dto.code() != null) {
            entity.setCode(dto.code());
        }
        if (dto.available() != null) {
            entity.setAvailable(dto.available());
        }
        if (dto.active() != null) {
            entity.setActive(dto.active());
        }

        entity = keyRepository.save(entity);
        return KeyGetDto.fromEntity(entity);
    }
}
