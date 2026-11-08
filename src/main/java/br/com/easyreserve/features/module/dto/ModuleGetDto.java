package br.com.easyreserve.features.module.dto;

import br.com.easyreserve.features.module.entity.ModuleEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ModuleGetDto(
        UUID id,
        String slug,
        String name,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ModuleGetDto fromEntity(ModuleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ModuleGetDto(
                entity.getId(),
                entity.getSlug(),
                entity.getName() != null ? entity.getName() : entity.getSlug(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
