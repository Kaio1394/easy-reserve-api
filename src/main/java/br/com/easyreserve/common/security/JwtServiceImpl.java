package br.com.easyreserve.common.security;

import br.com.easyreserve.features.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    @Override
    public String generate(UserEntity user) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuer("easyreserve")
                .setSubject(user.getId().toString())
                .claim("role", user.getRole().name())
                .claim("workspaceId", user.getWorkspace().getId().toString())
                .claim("workspaceSlug", user.getWorkspace().getSlug())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaims(token).getSubject());
    }

    public UUID extractWorkspaceId(String token) {
        return UUID.fromString(
                extractClaims(token).get("workspaceId", String.class)
        );
    }

    public String extractWorkspaceSlug(String token) {
        return extractClaims(token).get("workspaceSlug", String.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .requireIssuer("easyreserve")
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public long getExpiration() {
        return expiration;
    }
}
