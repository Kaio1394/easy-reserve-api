package br.com.easyreserve.features.workspace_module.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.module.dto.ModuleGetDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleCreateDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleDeleteDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleGetDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleUpdateDto;

import java.util.List;

public interface WorkspaceModuleService extends BaseService<WorkspaceModuleCreateDto, WorkspaceModuleUpdateDto, WorkspaceModuleGetDto, WorkspaceModuleDeleteDto> {
    List<ModuleGetDto> findAvailableModules();
}
