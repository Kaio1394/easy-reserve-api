package br.com.easyreserve.features.key.controller;

import br.com.easyreserve.features.key.dto.KeyCreateDto;
import br.com.easyreserve.features.key.dto.KeyDeleteDto;
import br.com.easyreserve.features.key.dto.KeyGetDto;
import br.com.easyreserve.features.key.dto.KeyUpdateDto;
import br.com.easyreserve.features.key.service.KeyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/keys")
public class KeyController {

    private final KeyService keyService;

    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @PostMapping
    public ResponseEntity<KeyGetDto> add(@Valid @RequestBody KeyCreateDto dto) {
        KeyGetDto response = keyService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<KeyGetDto>> findAll() {
        List<KeyGetDto> response = keyService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<KeyGetDto> update(@Valid @RequestBody KeyUpdateDto dto) {
        KeyGetDto response = keyService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody KeyDeleteDto dto) {
        keyService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
