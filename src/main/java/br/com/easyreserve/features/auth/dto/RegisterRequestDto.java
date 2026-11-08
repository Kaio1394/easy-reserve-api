package br.com.easyreserve.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record RegisterRequestDto(
        @NotBlank(message = "O nome do workspace é obrigatório")
        @Size(max = 255, message = "O nome do workspace deve ter no máximo 255 caracteres")
        String workspaceName,

        @NotBlank(message = "O slug do workspace é obrigatório")
        @Size(max = 255, message = "O slug deve ter no máximo 255 caracteres")
        String workspaceSlug,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String name,

        @NotBlank(message = "O nome de usuário é obrigatório")
        @Size(max = 255, message = "O nome de usuário deve ter no máximo 255 caracteres")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
        String password,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
        String email,

        @Size(max = 50, message = "O telefone deve ter no máximo 50 caracteres")
        String phone,

        @Size(max = 20, message = "O CPF deve ter no máximo 20 caracteres")
        String cpf,

        List<UUID> moduleIds
) {
}
