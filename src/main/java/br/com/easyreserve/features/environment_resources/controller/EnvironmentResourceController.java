package br.com.easyreserve.features.environment_resources.controller;

import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceCreateDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceDeleteDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceGetDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceUpdateDto;
import br.com.easyreserve.features.environment_resources.service.EnvironmentResourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/environment-resources")
public class EnvironmentResourceController {

    private final EnvironmentResourceService resourceService;

    public EnvironmentResourceController(EnvironmentResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<EnvironmentResourceGetDto> add(@Valid @RequestBody EnvironmentResourceCreateDto dto) {
        EnvironmentResourceGetDto response = resourceService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EnvironmentResourceGetDto>> findAll() {
        List<EnvironmentResourceGetDto> response = resourceService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<EnvironmentResourceGetDto> update(@Valid @RequestBody EnvironmentResourceUpdateDto dto) {
        EnvironmentResourceGetDto response = resourceService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody EnvironmentResourceDeleteDto dto) {
        resourceService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
