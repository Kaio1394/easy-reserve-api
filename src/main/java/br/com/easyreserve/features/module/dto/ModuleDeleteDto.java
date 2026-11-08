package br.com.easyreserve.features.module.dto;

import jakarta.validation.constraints.NotBlank;

public record ModuleDeleteDto(
        @NotBlank(message = "O slug do módulo é obrigatório")
        String slug
) {
}
