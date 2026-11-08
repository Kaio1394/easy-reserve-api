package br.com.easyreserve.features.workspace.service;

import br.com.easyreserve.exception.WorkspaceAlreadyExistsException;
import br.com.easyreserve.features.workspace.dto.WorkspaceCreateDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceDeleteDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceGetDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceUpdateDto;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    @Override
    public WorkspaceGetDto add(WorkspaceCreateDto workspaceCreateDto) {
        if (workspaceRepository.findWorkspaceBySlug(workspaceCreateDto.slug()).isPresent()) {
            throw new WorkspaceAlreadyExistsException(workspaceCreateDto.slug());
        }
        WorkspaceEntity wEntity = workspaceCreateDto.toEntity();
        wEntity = workspaceRepository.save(wEntity);
        return WorkspaceGetDto.fromEntity(wEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkspaceGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        return workspaceRepository.findAll()
                .stream()
                .filter(w -> Boolean.TRUE.equals(w.getActive()))
                .filter(w -> wsId == null || w.getId().equals(wsId))
                .map(WorkspaceGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void delete(WorkspaceDeleteDto workspaceDeleteDto) {
        WorkspaceEntity wEntity = workspaceRepository.findById(workspaceDeleteDto.id())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));
        workspaceRepository.delete(wEntity);
    }

    @Transactional
    @Override
    public WorkspaceGetDto update(WorkspaceUpdateDto workspaceUpdateDto) {
        WorkspaceEntity wEntity = workspaceRepository.findById(workspaceUpdateDto.id())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        if (workspaceUpdateDto.name() != null) {
            wEntity.setName(workspaceUpdateDto.name());
        }
        if (workspaceUpdateDto.slug() != null && !workspaceUpdateDto.slug().equals(wEntity.getSlug())) {
            if (workspaceRepository.findWorkspaceBySlug(workspaceUpdateDto.slug()).isPresent()) {
                throw new WorkspaceAlreadyExistsException(workspaceUpdateDto.slug());
            }
            wEntity.setSlug(workspaceUpdateDto.slug());
        }
        if (workspaceUpdateDto.active() != null) {
            wEntity.setActive(workspaceUpdateDto.active());
        }

        wEntity = workspaceRepository.save(wEntity);
        return WorkspaceGetDto.fromEntity(wEntity);
    }
}
