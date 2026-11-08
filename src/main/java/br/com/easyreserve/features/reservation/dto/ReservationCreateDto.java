package br.com.easyreserve.features.reservation.dto;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationCreateDto(
        @NotNull(message = "O ID do workspace é obrigatório")
        UUID workspaceId,

        @NotNull(message = "O ID do ambiente é obrigatório")
        UUID environmentId,

        @NotNull(message = "A chave utilizada na reserva é obrigatória")
        UUID keyId,

        UUID userId,

        @Size(max = 255, message = "A finalidade deve ter no máximo 255 caracteres")
        String purpose,

        LocalDateTime startTime,

        LocalDateTime endTime
) {
    public ReservationEntity toEntity(
            WorkspaceEntity workspace,
            EnvironmentEntity environment,
            KeyEntity key,
            UserEntity user
    ) {
        ReservationEntity entity = new ReservationEntity();
        entity.setWorkspace(workspace);
        entity.setEnvironment(environment);
        entity.setKey(key);
        entity.setUser(user);
        entity.setPurpose(this.purpose);
        entity.setStartTime(this.startTime != null ? this.startTime : LocalDateTime.now());
        entity.setEndTime(this.endTime);
        entity.setStatus(ReservationStatus.ACTIVE);
        entity.setActive(Boolean.TRUE);
        return entity;
    }
}
