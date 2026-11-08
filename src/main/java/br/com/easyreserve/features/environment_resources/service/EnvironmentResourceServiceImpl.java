package br.com.easyreserve.features.environment_resources.service;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.environment.repository.EnvironmentRepository;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceCreateDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceDeleteDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceGetDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceUpdateDto;
import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;
import br.com.easyreserve.features.environment_resources.repository.EnvironmentResourceRepository;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EnvironmentResourceServiceImpl implements EnvironmentResourceService {

    private final EnvironmentResourceRepository resourceRepository;
    private final WorkspaceRepository workspaceRepository;
    private final EnvironmentRepository environmentRepository;

    public EnvironmentResourceServiceImpl(EnvironmentResourceRepository resourceRepository,
                                          WorkspaceRepository workspaceRepository,
                                          EnvironmentRepository environmentRepository) {
        this.resourceRepository = resourceRepository;
        this.workspaceRepository = workspaceRepository;
        this.environmentRepository = environmentRepository;
    }

    @Transactional
    @Override
    public EnvironmentResourceGetDto add(EnvironmentResourceCreateDto dto) {
        WorkspaceEntity workspace = workspaceRepository.findById(dto.workspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        EnvironmentEntity environment = null;
        if (dto.environmentId() != null) {
            environment = environmentRepository.findById(dto.environmentId())
                    .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));
        }

        EnvironmentResourceEntity entity = dto.toEntity(workspace, environment);
        entity = resourceRepository.save(entity);
        return EnvironmentResourceGetDto.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnvironmentResourceGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        List<EnvironmentResourceEntity> resources = wsId != null
                ? resourceRepository.findByWorkspaceId(wsId)
                : resourceRepository.findAllWithDetails();
        return resources.stream()
                .filter(r -> Boolean.TRUE.equals(r.getActive()))
                .map(EnvironmentResourceGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void delete(EnvironmentResourceDeleteDto dto) {
        EnvironmentResourceEntity entity = resourceRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Recurso/elemento não encontrado"));
        // Soft Delete: desativa o recurso preservando o histórico
        entity.setActive(Boolean.FALSE);
        resourceRepository.save(entity);
    }

    @Transactional
    @Override
    public EnvironmentResourceGetDto update(EnvironmentResourceUpdateDto dto) {
        EnvironmentResourceEntity entity = resourceRepository.findByIdWithDetails(dto.id())
                .orElseThrow(() -> new RuntimeException("Recurso/elemento não encontrado"));

        if (dto.environmentId() != null) {
            EnvironmentEntity environment = environmentRepository.findById(dto.environmentId())
                    .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));
            entity.setEnvironment(environment);
            if (dto.quantity() != null) {
                entity.setQuantity(dto.quantity());
            } else if (entity.getQuantity() == null) {
                entity.setQuantity(1);
            }
        } else {
            entity.setEnvironment(null);
            entity.setQuantity(dto.quantity());
        }

        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.weight() != null) {
            entity.setWeight(dto.weight());
            // Se for item de catálogo (environment == null), sincroniza o peso para todas as alocações daquele recurso no mesmo workspace
            if (entity.getEnvironment() == null && entity.getWorkspace() != null) {
                final UUID wsId = entity.getWorkspace().getId();
                final String resName = entity.getName();
                List<EnvironmentResourceEntity> allocations = resourceRepository.findAll()
                        .stream()
                        .filter(r -> r.getWorkspace() != null
                                && r.getWorkspace().getId().equals(wsId)
                                && r.getName().equalsIgnoreCase(resName)
                                && r.getEnvironment() != null)
                        .toList();
                for (EnvironmentResourceEntity alloc : allocations) {
                    alloc.setWeight(dto.weight());
                    resourceRepository.save(alloc);
                }
            }
        }
        if (dto.operational() != null) {
            entity.setOperational(dto.operational());
        }
        if (dto.active() != null) {
            entity.setActive(dto.active());
        }

        entity = resourceRepository.save(entity);
        return EnvironmentResourceGetDto.fromEntity(entity);
    }
}
