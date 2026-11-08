package br.com.easyreserve.features.workspace.repository;

import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {
    @Query("SELECT w FROM WorkspaceEntity w WHERE w.slug = :slug")
    Optional<WorkspaceEntity> findWorkspaceBySlug(@Param("slug") String slug);
}
