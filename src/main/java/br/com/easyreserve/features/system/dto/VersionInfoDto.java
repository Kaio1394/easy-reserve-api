package br.com.easyreserve.features.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações de Versão e Build da API")
public record VersionInfoDto(
        @Schema(description = "Nome do sistema", example = "easy-reserve-api")
        String app,

        @Schema(description = "Versão semântica da aplicação", example = "0.1.3")
        String version,

        @Schema(description = "Número da execução / Build number", example = "4")
        String buildNumber,

        @Schema(description = "Hash SHA do commit Git", example = "9f0b8d1")
        String gitCommit,

        @Schema(description = "Timestamp do momento do build / deploy", example = "2026-11-08T14:45:00Z")
        String buildTime,

        @Schema(description = "Ambiente de execução", example = "production")
        String environment
) {}
