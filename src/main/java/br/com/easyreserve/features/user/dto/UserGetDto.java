package br.com.easyreserve.features.user.dto;

import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.user.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserGetDto(
        UUID id,
        UUID workspaceId,
        String workspaceSlug,
        String workspaceName,
        String name,
        String username,
        String email,
        String phone,
        String cpf,
        Role role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserGetDto fromEntity(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserGetDto(
                entity.getId(),
                entity.getWorkspace() != null ? entity.getWorkspace().getId() : null,
                entity.getWorkspace() != null ? entity.getWorkspace().getSlug() : null,
                entity.getWorkspace() != null ? entity.getWorkspace().getName() : null,
                entity.getName(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getCpf(),
                entity.getRole(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
