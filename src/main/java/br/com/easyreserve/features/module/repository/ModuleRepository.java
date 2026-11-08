package br.com.easyreserve.features.module.repository;

import br.com.easyreserve.features.module.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleEntity, UUID> {
    @Query("SELECT m FROM ModuleEntity m WHERE m.slug = :slug")
    Optional<ModuleEntity> findModuleBySlug(@Param("slug") String slug);
}
