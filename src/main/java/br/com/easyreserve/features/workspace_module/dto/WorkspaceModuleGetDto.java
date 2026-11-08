package br.com.easyreserve.features.workspace_module.dto;

import br.com.easyreserve.features.workspace_module.entity.WorkspaceModuleEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkspaceModuleGetDto(
        UUID id,
        UUID workspaceId,
        UUID moduleId,
        String moduleSlug,
        String moduleName,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static WorkspaceModuleGetDto fromEntity(WorkspaceModuleEntity entity) {
        if (entity == null) {
            return null;
        }
        String slug = entity.getModule() != null ? entity.getModule().getSlug() : null;
        String name = entity.getModule() != null && entity.getModule().getName() != null
                ? entity.getModule().getName()
                : slug;
        return new WorkspaceModuleGetDto(
                entity.getId(),
                entity.getWorkspace() != null ? entity.getWorkspace().getId() : null,
                entity.getModule() != null ? entity.getModule().getId() : null,
                slug,
                name,
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
