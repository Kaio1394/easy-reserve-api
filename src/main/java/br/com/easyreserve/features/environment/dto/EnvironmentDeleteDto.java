package br.com.easyreserve.features.environment.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnvironmentDeleteDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id
) {
}
