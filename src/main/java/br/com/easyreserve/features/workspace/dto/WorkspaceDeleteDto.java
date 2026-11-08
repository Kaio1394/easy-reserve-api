package br.com.easyreserve.features.workspace.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkspaceDeleteDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id
) {
}
