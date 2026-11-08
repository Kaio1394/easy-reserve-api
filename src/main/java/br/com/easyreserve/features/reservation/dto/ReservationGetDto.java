package br.com.easyreserve.features.reservation.dto;

import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationGetDto(
        UUID id,
        UUID workspaceId,
        UUID environmentId,
        String environmentName,
        UUID keyId,
        String keyCode,
        Boolean keyAvailable,
        UUID userId,
        String userName,
        String purpose,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReservationGetDto fromEntity(ReservationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ReservationGetDto(
                entity.getId(),
                entity.getWorkspace() != null ? entity.getWorkspace().getId() : null,
                entity.getEnvironment() != null ? entity.getEnvironment().getId() : null,
                entity.getEnvironment() != null ? entity.getEnvironment().getName() : null,
                entity.getKey() != null ? entity.getKey().getId() : null,
                entity.getKey() != null ? entity.getKey().getCode() : null,
                entity.getKey() != null ? entity.getKey().getAvailable() : null,
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getUser() != null ? entity.getUser().getName() : null,
                entity.getPurpose(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
