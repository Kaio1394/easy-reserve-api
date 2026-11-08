package br.com.easyreserve.features.key.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record KeyDeleteDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id
) {
}
