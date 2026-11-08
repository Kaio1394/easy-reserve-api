package br.com.easyreserve.features.module.dto;

import br.com.easyreserve.features.module.entity.ModuleEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ModuleCreateDto(
        @NotBlank(message = "O slug do módulo é obrigatório")
        @Size(max = 255, message = "O slug deve ter no máximo 255 caracteres")
        String slug,

        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name
) {
    public ModuleEntity toEntity() {
        ModuleEntity mEntity = new ModuleEntity();
        mEntity.setSlug(this.slug);
        mEntity.setName(this.name != null && !this.name.isBlank() ? this.name : this.slug);
        return mEntity;
    }
}
