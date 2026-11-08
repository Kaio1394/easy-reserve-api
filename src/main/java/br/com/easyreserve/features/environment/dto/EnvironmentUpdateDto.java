package br.com.easyreserve.features.environment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EnvironmentUpdateDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacity,

        Boolean active
) {
}
