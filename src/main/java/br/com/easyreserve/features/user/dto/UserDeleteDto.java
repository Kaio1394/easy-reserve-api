package br.com.easyreserve.features.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDeleteDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id
) {
}
