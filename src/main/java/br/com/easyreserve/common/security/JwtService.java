package br.com.easyreserve.common.security;

import br.com.easyreserve.features.user.entity.UserEntity;

import java.util.UUID;

public interface JwtService {
    String generate(UserEntity user);
    boolean isValid(String token);
    long getExpiration();
    UUID extractUserId(String token);
}
