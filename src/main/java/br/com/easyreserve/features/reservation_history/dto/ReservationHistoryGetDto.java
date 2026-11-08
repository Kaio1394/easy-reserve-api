package br.com.easyreserve.features.reservation_history.dto;

import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.reservation_history.entity.ReservationHistoryEntity;
import br.com.easyreserve.features.reservation_history.enums.ReservationHistoryAction;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationHistoryGetDto(
        UUID id,
        UUID workspaceId,
        UUID reservationId,
        UUID environmentId,
        String environmentName,
        UUID keyId,
        String keyCode,
        UUID userId,
        String userName,
        ReservationHistoryAction action,
        String purpose,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        String notes,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReservationHistoryGetDto fromEntity(ReservationHistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ReservationHistoryGetDto(
                entity.getId(),
                entity.getWorkspace() != null ? entity.getWorkspace().getId() : null,
                entity.getReservation() != null ? entity.getReservation().getId() : null,
                entity.getEnvironment() != null ? entity.getEnvironment().getId() : null,
                entity.getEnvironment() != null ? entity.getEnvironment().getName() : null,
                entity.getKey() != null ? entity.getKey().getId() : null,
                entity.getKey() != null ? entity.getKey().getCode() : null,
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getUser() != null ? entity.getUser().getName() : null,
                entity.getAction(),
                entity.getPurpose(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
