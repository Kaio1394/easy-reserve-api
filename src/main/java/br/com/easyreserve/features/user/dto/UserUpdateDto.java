package br.com.easyreserve.features.user.dto;

import br.com.easyreserve.features.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserUpdateDto(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @Size(max = 255, message = "O nome de usuário deve ter no máximo 255 caracteres")
        String username,

        @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
        String password,

        @Email(message = "Formato de e-mail inválido")
        @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
        String email,

        @Size(max = 50, message = "O telefone deve ter no máximo 50 caracteres")
        String phone,

        @Size(max = 20, message = "O CPF deve ter no máximo 20 caracteres")
        String cpf,

        Role role,

        Boolean active
) {
}
