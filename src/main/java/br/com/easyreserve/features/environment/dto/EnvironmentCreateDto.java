package br.com.easyreserve.features.environment.dto;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EnvironmentCreateDto(
        @NotNull(message = "O ID do workspace é obrigatório")
        UUID workspaceId,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacity
) {
    public EnvironmentEntity toEntity(WorkspaceEntity workspace) {
        EnvironmentEntity entity = new EnvironmentEntity();
        entity.setWorkspace(workspace);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setCapacity(this.capacity);
        return entity;
    }
}
