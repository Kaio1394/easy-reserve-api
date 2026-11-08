package br.com.easyreserve.features.workspace_module.repository;

import br.com.easyreserve.features.workspace_module.entity.WorkspaceModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceModuleRepository extends JpaRepository<WorkspaceModuleEntity, UUID> {
    @Query("SELECT wm FROM WorkspaceModuleEntity wm JOIN FETCH wm.module JOIN FETCH wm.workspace WHERE wm.workspace.id = :workspaceId AND wm.module.id = :moduleId")
    Optional<WorkspaceModuleEntity> findByWorkspaceIdAndModuleId(
            @Param("workspaceId") UUID workspaceId,
            @Param("moduleId") UUID moduleId
    );

    @Query("SELECT wm FROM WorkspaceModuleEntity wm JOIN FETCH wm.module JOIN FETCH wm.workspace WHERE wm.workspace.id = :workspaceId ORDER BY wm.module.slug ASC")
    List<WorkspaceModuleEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT wm FROM WorkspaceModuleEntity wm JOIN FETCH wm.module JOIN FETCH wm.workspace ORDER BY wm.module.slug ASC")
    List<WorkspaceModuleEntity> findAllWithDetails();
}
