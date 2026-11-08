package br.com.easyreserve.features.workspace_module.dto;

import br.com.easyreserve.features.module.entity.ModuleEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace_module.entity.WorkspaceModuleEntity;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkspaceModuleCreateDto(
        @NotNull(message = "O ID do workspace é obrigatório")
        UUID workspaceId,

        @NotNull(message = "O ID do módulo é obrigatório")
        UUID moduleId
) {
    public WorkspaceModuleEntity toEntity(WorkspaceEntity workspace, ModuleEntity module) {
        WorkspaceModuleEntity entity = new WorkspaceModuleEntity();
        entity.setWorkspace(workspace);
        entity.setModule(module);
        return entity;
    }
}
