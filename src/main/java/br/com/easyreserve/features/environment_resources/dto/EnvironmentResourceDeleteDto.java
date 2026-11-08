package br.com.easyreserve.features.environment_resources.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnvironmentResourceDeleteDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id
) {
}
