package br.com.easyreserve.features.workspace_module.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkspaceModuleUpdateDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean active
) {
}
