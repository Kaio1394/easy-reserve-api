package br.com.easyreserve.features.workspace_module.service;

import br.com.easyreserve.features.module.dto.ModuleGetDto;
import br.com.easyreserve.features.module.entity.ModuleEntity;
import br.com.easyreserve.features.module.repository.ModuleRepository;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleCreateDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleDeleteDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleGetDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleUpdateDto;
import br.com.easyreserve.features.workspace_module.entity.WorkspaceModuleEntity;
import br.com.easyreserve.features.workspace_module.repository.WorkspaceModuleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class WorkspaceModuleServiceImpl implements WorkspaceModuleService {

    private final WorkspaceModuleRepository workspaceModuleRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ModuleRepository moduleRepository;

    public WorkspaceModuleServiceImpl(WorkspaceModuleRepository workspaceModuleRepository,
                                      WorkspaceRepository workspaceRepository,
                                      ModuleRepository moduleRepository) {
        this.workspaceModuleRepository = workspaceModuleRepository;
        this.workspaceRepository = workspaceRepository;
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    @Override
    public WorkspaceModuleGetDto add(WorkspaceModuleCreateDto dto) {
        if (workspaceModuleRepository.findByWorkspaceIdAndModuleId(dto.workspaceId(), dto.moduleId()).isPresent()) {
            throw new RuntimeException("O módulo já está vinculado a este workspace");
        }

        WorkspaceEntity workspace = workspaceRepository.findById(dto.workspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        ModuleEntity module = moduleRepository.findById(dto.moduleId())
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        WorkspaceModuleEntity entity = dto.toEntity(workspace, module);
        entity = workspaceModuleRepository.save(entity);
        return WorkspaceModuleGetDto.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkspaceModuleGetDto> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserEntity user && user.getWorkspace() != null) {
            return workspaceModuleRepository.findByWorkspaceId(user.getWorkspace().getId())
                    .stream()
                    .map(WorkspaceModuleGetDto::fromEntity)
                    .toList();
        }
        return workspaceModuleRepository.findAllWithDetails()
                .stream()
                .map(WorkspaceModuleGetDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ModuleGetDto> findAvailableModules() {
        return moduleRepository.findAll()
                .stream()
                .filter(m -> Boolean.TRUE.equals(m.getActive()))
                .sorted(Comparator.comparing(ModuleEntity::getSlug))
                .map(ModuleGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void delete(WorkspaceModuleDeleteDto dto) {
        WorkspaceModuleEntity entity = workspaceModuleRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Vínculo entre workspace e módulo não encontrado"));
        workspaceModuleRepository.delete(entity);
    }

    @Transactional
    @Override
    public WorkspaceModuleGetDto update(WorkspaceModuleUpdateDto dto) {
        WorkspaceModuleEntity entity = workspaceModuleRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Vínculo entre workspace e módulo não encontrado"));
        entity.setActive(dto.active());
        entity = workspaceModuleRepository.save(entity);
        return WorkspaceModuleGetDto.fromEntity(entity);
    }
}
