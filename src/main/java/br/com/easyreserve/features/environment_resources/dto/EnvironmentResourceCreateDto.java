package br.com.easyreserve.features.environment_resources.dto;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EnvironmentResourceCreateDto(
        @NotNull(message = "O ID do workspace é obrigatório")
        UUID workspaceId,

        UUID environmentId,

        @NotBlank(message = "O nome do elemento/recurso é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity,

        Double weight,

        Boolean operational
) {
    public EnvironmentResourceEntity toEntity(WorkspaceEntity workspace, EnvironmentEntity environment) {
        EnvironmentResourceEntity entity = new EnvironmentResourceEntity();
        entity.setWorkspace(workspace);
        entity.setEnvironment(environment);
        entity.setName(this.name);
        if (environment != null) {
            entity.setQuantity(this.quantity != null ? this.quantity : Integer.valueOf(1));
        } else {
            entity.setQuantity(this.quantity);
        }
        entity.setWeight(this.weight != null ? this.weight : 1.0);
        entity.setOperational(this.operational != null ? this.operational : Boolean.TRUE);
        return entity;
    }
}
