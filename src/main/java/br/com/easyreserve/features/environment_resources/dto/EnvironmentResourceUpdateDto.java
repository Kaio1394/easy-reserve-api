package br.com.easyreserve.features.environment_resources.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EnvironmentResourceUpdateDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        UUID environmentId,

        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity,

        Double weight,

        Boolean operational,

        Boolean active
) {
}
