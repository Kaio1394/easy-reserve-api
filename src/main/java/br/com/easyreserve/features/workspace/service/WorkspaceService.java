package br.com.easyreserve.features.workspace.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.workspace.dto.WorkspaceCreateDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceDeleteDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceGetDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceUpdateDto;

public interface WorkspaceService extends BaseService<WorkspaceCreateDto, WorkspaceUpdateDto, WorkspaceGetDto, WorkspaceDeleteDto> {
}
