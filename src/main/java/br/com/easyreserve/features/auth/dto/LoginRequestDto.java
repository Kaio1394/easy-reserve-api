package br.com.easyreserve.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank(message = "O login (username ou e-mail) é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {
}
