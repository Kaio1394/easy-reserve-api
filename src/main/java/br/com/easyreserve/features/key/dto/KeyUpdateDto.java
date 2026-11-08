package br.com.easyreserve.features.key.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record KeyUpdateDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        @Size(max = 100, message = "O código deve ter no máximo 100 caracteres")
        String code,

        Boolean available,

        Boolean active
) {
}
