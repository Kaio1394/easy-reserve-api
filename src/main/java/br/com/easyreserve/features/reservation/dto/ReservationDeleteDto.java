package br.com.easyreserve.features.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReservationDeleteDto(
        @NotNull(message = "O ID da reserva é obrigatório")
        UUID id
) {
}
