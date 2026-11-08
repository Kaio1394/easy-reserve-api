package br.com.easyreserve.features.environment.controller;

import br.com.easyreserve.features.environment.dto.*;
import br.com.easyreserve.features.environment.service.EnvironmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/environments")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @PostMapping
    public ResponseEntity<EnvironmentGetDto> add(@Valid @RequestBody EnvironmentCreateDto dto) {
        EnvironmentGetDto response = environmentService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("recommend")
    public ResponseEntity<List<EnvironmentRecommendationItemDto>> recommend(@Valid @RequestBody EnvironmentRecommendationRequestDto dto) {
        List<EnvironmentRecommendationItemDto> response = environmentService.recommend(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EnvironmentGetDto>> findAll() {
        List<EnvironmentGetDto> response = environmentService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<EnvironmentGetDto> update(@Valid @RequestBody EnvironmentUpdateDto dto) {
        EnvironmentGetDto response = environmentService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody EnvironmentDeleteDto dto) {
        environmentService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
