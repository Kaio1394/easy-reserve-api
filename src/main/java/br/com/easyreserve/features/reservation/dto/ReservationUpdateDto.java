package br.com.easyreserve.features.reservation.dto;

import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationUpdateDto(
        @NotNull(message = "O ID da reserva é obrigatório")
        UUID id,

        @Size(max = 255, message = "A finalidade deve ter no máximo 255 caracteres")
        String purpose,

        LocalDateTime startTime,

        LocalDateTime endTime,

        ReservationStatus status,

        Boolean active
) {
}
