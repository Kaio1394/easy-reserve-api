package br.com.easyreserve.features.system.controller;

import br.com.easyreserve.features.system.dto.VersionInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("version")
@Tag(name = "Sistema", description = "Endpoints de infraestrutura, versão e integridade")
public class VersionController {

    @Value("${app.name:easy-reserve-api}")
    private String appName;

    @Value("${app.version:0.1.0}")
    private String version;

    @Value("${app.build-number:local}")
    private String buildNumber;

    @Value("${app.git-commit:dev}")
    private String gitCommit;

    @Value("${app.build-time:}")
    private String buildTime;

    @Value("${app.environment:production}")
    private String environment;

    @GetMapping
    @Operation(summary = "Consultar versão, build number e commit atual da API")
    public ResponseEntity<VersionInfoDto> getVersion() {
        return ResponseEntity.ok(new VersionInfoDto(
                appName,
                version,
                buildNumber,
                gitCommit,
                buildTime.isBlank() ? "local-build" : buildTime,
                environment
        ));
    }
}
