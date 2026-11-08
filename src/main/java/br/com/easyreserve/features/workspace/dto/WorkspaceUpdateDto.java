package br.com.easyreserve.features.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record WorkspaceUpdateDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @NotBlank(message = "O slug é obrigatório")
        @Size(max = 255, message = "O slug deve ter no máximo 255 caracteres")
        String slug,

        Boolean active
) {
}
