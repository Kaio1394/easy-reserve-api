package br.com.easyreserve.features.workspace_module.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkspaceModuleDeleteDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id
) {
}
