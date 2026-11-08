package br.com.easyreserve.features.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModuleUpdateDto(
        @NotBlank(message = "O slug do módulo é obrigatório")
        @Size(max = 255, message = "O slug deve ter no máximo 255 caracteres")
        String slug,

        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean active
) {
}
