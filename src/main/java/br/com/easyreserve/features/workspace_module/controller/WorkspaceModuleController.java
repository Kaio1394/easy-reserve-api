package br.com.easyreserve.features.workspace_module.controller;

import br.com.easyreserve.features.module.dto.ModuleGetDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleCreateDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleDeleteDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleGetDto;
import br.com.easyreserve.features.workspace_module.dto.WorkspaceModuleUpdateDto;
import br.com.easyreserve.features.workspace_module.service.WorkspaceModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/workspace-modules")
public class WorkspaceModuleController {

    private final WorkspaceModuleService workspaceModuleService;

    public WorkspaceModuleController(WorkspaceModuleService workspaceModuleService) {
        this.workspaceModuleService = workspaceModuleService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceModuleGetDto> add(@Valid @RequestBody WorkspaceModuleCreateDto dto) {
        WorkspaceModuleGetDto response = workspaceModuleService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceModuleGetDto>> findAll() {
        List<WorkspaceModuleGetDto> response = workspaceModuleService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/available", "/modules"})
    public ResponseEntity<List<ModuleGetDto>> findAvailableModules() {
        List<ModuleGetDto> response = workspaceModuleService.findAvailableModules();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<WorkspaceModuleGetDto> update(@Valid @RequestBody WorkspaceModuleUpdateDto dto) {
        WorkspaceModuleGetDto response = workspaceModuleService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody WorkspaceModuleDeleteDto dto) {
        workspaceModuleService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
