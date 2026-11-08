package br.com.easyreserve.features.environment.repository;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnvironmentRepository extends JpaRepository<EnvironmentEntity, UUID> {
    @Query("SELECT e FROM EnvironmentEntity e JOIN FETCH e.workspace")
    List<EnvironmentEntity> findAllWithWorkspace();

    @Query("SELECT e FROM EnvironmentEntity e JOIN FETCH e.workspace WHERE e.workspace.id = :workspaceId AND e.active = true")
    List<EnvironmentEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT e FROM EnvironmentEntity e JOIN FETCH e.workspace WHERE e.id = :id")
    Optional<EnvironmentEntity> findByIdWithWorkspace(@Param("id") UUID id);

    @Query("SELECT e FROM EnvironmentEntity e JOIN FETCH e.workspace WHERE e.workspace.id = :workspaceId AND e.name = :name")
    Optional<EnvironmentEntity> findByWorkspaceIdAndName(
            @Param("workspaceId") UUID workspaceId,
            @Param("name") String name
    );
}
