package br.com.easyreserve.features.user.dto;

import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserCreateDto(
        @NotNull(message = "O ID do workspace é obrigatório")
        UUID workspaceId,

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

        @NotNull(message = "O perfil (role) é obrigatório")
        Role role
) {

        public UserEntity toEntity(WorkspaceEntity workspace) {
                UserEntity entity = new UserEntity();
                entity.setName(this.name);
                entity.setUsername(this.username);
                entity.setPassword(this.password);
                entity.setEmail(this.email);
                entity.setPhone(this.phone);
                entity.setCpf(this.cpf);
                entity.setRole(this.role);
                entity.setWorkspace(workspace);
                return entity;
        }
}
