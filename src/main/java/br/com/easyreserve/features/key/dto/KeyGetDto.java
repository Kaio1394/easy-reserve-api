package br.com.easyreserve.features.key.dto;

import br.com.easyreserve.features.key.entity.KeyEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record KeyGetDto(
        UUID id,
        UUID environmentId,
        String code,
        Boolean available,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static KeyGetDto fromEntity(KeyEntity entity) {
        if (entity == null) {
            return null;
        }
        return new KeyGetDto(
                entity.getId(),
                entity.getEnvironment() != null ? entity.getEnvironment().getId() : null,
                entity.getCode(),
                entity.getAvailable(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
