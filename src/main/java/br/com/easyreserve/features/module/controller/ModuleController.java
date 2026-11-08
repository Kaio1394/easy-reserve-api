package br.com.easyreserve.features.module.controller;

import br.com.easyreserve.features.module.dto.ModuleCreateDto;
import br.com.easyreserve.features.module.dto.ModuleDeleteDto;
import br.com.easyreserve.features.module.dto.ModuleGetDto;
import br.com.easyreserve.features.module.dto.ModuleUpdateDto;
import br.com.easyreserve.features.module.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping
    public ResponseEntity<ModuleGetDto> add(@Valid @RequestBody ModuleCreateDto moduleCreateDto) {
        ModuleGetDto response = moduleService.add(moduleCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ModuleGetDto>> findAll() {
        List<ModuleGetDto> response = moduleService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ModuleGetDto> update(@Valid @RequestBody ModuleUpdateDto moduleUpdateDto) {
        ModuleGetDto response = moduleService.update(moduleUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody ModuleDeleteDto moduleDeleteDto) {
        moduleService.delete(moduleDeleteDto);
        return ResponseEntity.noContent().build();
    }
}
