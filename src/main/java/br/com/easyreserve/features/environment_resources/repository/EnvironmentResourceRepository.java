package br.com.easyreserve.features.environment_resources.repository;

import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnvironmentResourceRepository extends JpaRepository<EnvironmentResourceEntity, UUID> {
    @Query("SELECT r FROM EnvironmentResourceEntity r JOIN FETCH r.workspace LEFT JOIN FETCH r.environment")
    List<EnvironmentResourceEntity> findAllWithDetails();

    @Query("SELECT r FROM EnvironmentResourceEntity r JOIN FETCH r.workspace LEFT JOIN FETCH r.environment WHERE r.workspace.id = :workspaceId AND r.active = true")
    List<EnvironmentResourceEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT r FROM EnvironmentResourceEntity r JOIN FETCH r.workspace LEFT JOIN FETCH r.environment WHERE r.id = :id")
    Optional<EnvironmentResourceEntity> findByIdWithDetails(@Param("id") UUID id);
}
