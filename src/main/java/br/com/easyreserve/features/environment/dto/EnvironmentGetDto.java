package br.com.easyreserve.features.environment.dto;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnvironmentGetDto(
        UUID id,
        UUID workspaceId,
        String workspaceSlug,
        String name,
        String description,
        Integer capacity,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EnvironmentGetDto fromEntity(EnvironmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return new EnvironmentGetDto(
                entity.getId(),
                entity.getWorkspace() != null ? entity.getWorkspace().getId() : null,
                entity.getWorkspace() != null ? entity.getWorkspace().getSlug() : null,
                entity.getName(),
                entity.getDescription(),
                entity.getCapacity(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
