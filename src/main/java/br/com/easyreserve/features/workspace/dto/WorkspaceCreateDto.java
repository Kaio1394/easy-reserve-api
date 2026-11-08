package br.com.easyreserve.features.workspace.dto;

import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkspaceCreateDto(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @NotBlank(message = "O slug é obrigatório")
        @Size(max = 255, message = "O slug deve ter no máximo 255 caracteres")
        String slug
) {
        public WorkspaceEntity toEntity(){
                WorkspaceEntity entity = new WorkspaceEntity();
                entity.setName(this.name);
                entity.setSlug(this.slug);
                return entity;
        }
}
