package br.com.easyreserve.common.security;

import br.com.easyreserve.features.user.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<UserEntity> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserEntity user) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public static UUID getCurrentWorkspaceId() {
        return getCurrentUser()
                .map(UserEntity::getWorkspace)
                .map(w -> w.getId())
                .orElse(null);
    }
}
