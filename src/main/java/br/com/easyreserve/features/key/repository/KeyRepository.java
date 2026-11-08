package br.com.easyreserve.features.key.repository;

import br.com.easyreserve.features.key.entity.KeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeyRepository extends JpaRepository<KeyEntity, UUID> {
    @Query("SELECT k FROM KeyEntity k WHERE k.environment.id = :environmentId AND LOWER(TRIM(k.code)) = LOWER(TRIM(:code))")
    Optional<KeyEntity> findByEnvironmentIdAndCode(
            @Param("environmentId") UUID environmentId,
            @Param("code") String code
    );

    @Query("SELECT k FROM KeyEntity k WHERE k.environment.id = :environmentId AND k.active = true")
    List<KeyEntity> findByEnvironmentId(@Param("environmentId") UUID environmentId);

    @Query("SELECT k FROM KeyEntity k JOIN FETCH k.environment e JOIN FETCH e.workspace w WHERE w.id = :workspaceId AND k.active = true")
    List<KeyEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT k FROM KeyEntity k JOIN FETCH k.environment e JOIN FETCH e.workspace w WHERE k.active = true")
    List<KeyEntity> findAllWithDetails();
}
