package br.com.easyreserve.features.environment_resources.dto;

import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnvironmentResourceGetDto(
        UUID id,
        UUID workspaceId,
        UUID environmentId,
        String environmentName,
        String name,
        Integer quantity,
        Double weight,
        Boolean operational,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EnvironmentResourceGetDto fromEntity(EnvironmentResourceEntity entity) {
        if (entity == null) {
            return null;
        }
        return new EnvironmentResourceGetDto(
                entity.getId(),
                entity.getWorkspace() != null ? entity.getWorkspace().getId() : null,
                entity.getEnvironment() != null ? entity.getEnvironment().getId() : null,
                entity.getEnvironment() != null ? entity.getEnvironment().getName() : null,
                entity.getName(),
                entity.getQuantity(),
                entity.getWeight() != null ? entity.getWeight() : 1.0,
                entity.getOperational(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
