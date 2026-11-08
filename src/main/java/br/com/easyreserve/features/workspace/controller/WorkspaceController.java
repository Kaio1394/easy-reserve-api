package br.com.easyreserve.features.workspace.controller;

import br.com.easyreserve.features.workspace.dto.WorkspaceCreateDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceDeleteDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceGetDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceUpdateDto;
import br.com.easyreserve.features.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceGetDto> add(@Valid @RequestBody WorkspaceCreateDto dto) {
        WorkspaceGetDto response = workspaceService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceGetDto>> findAll() {
        List<WorkspaceGetDto> response = workspaceService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<WorkspaceGetDto> update(@Valid @RequestBody WorkspaceUpdateDto dto) {
        WorkspaceGetDto response = workspaceService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody WorkspaceDeleteDto dto) {
        workspaceService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
