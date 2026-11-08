package br.com.easyreserve.features.key.dto;

import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.key.entity.KeyEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record KeyCreateDto(
        @NotNull(message = "O ID do ambiente é obrigatório")
        UUID environmentId,

        @NotBlank(message = "O código é obrigatório")
        @Size(max = 100, message = "O código deve ter no máximo 100 caracteres")
        String code
) {
    public KeyEntity toEntity(EnvironmentEntity environment) {
        KeyEntity entity = new KeyEntity();
        entity.setEnvironment(environment);
        entity.setCode(this.code);
        return entity;
    }
}
