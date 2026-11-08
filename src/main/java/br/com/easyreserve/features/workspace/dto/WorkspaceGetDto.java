package br.com.easyreserve.features.workspace.dto;

import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkspaceGetDto(
        UUID id,
        String name,
        String slug,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static WorkspaceGetDto fromEntity(WorkspaceEntity entity) {
        if (entity == null) {
            return null;
        }
        return new WorkspaceGetDto(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
