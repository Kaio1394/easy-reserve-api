package br.com.easyreserve.features.user.controller;

import br.com.easyreserve.features.user.dto.UserCreateDto;
import br.com.easyreserve.features.user.dto.UserDeleteDto;
import br.com.easyreserve.features.user.dto.UserGetDto;
import br.com.easyreserve.features.user.dto.UserProfileUpdateDto;
import br.com.easyreserve.features.user.dto.UserUpdateDto;
import br.com.easyreserve.features.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserGetDto> add(@Valid @RequestBody UserCreateDto dto) {
        UserGetDto response = userService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserGetDto>> findAll() {
        List<UserGetDto> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserGetDto> update(@Valid @RequestBody UserUpdateDto dto) {
        UserGetDto response = userService.update(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("profile")
    public ResponseEntity<UserGetDto> updateProfile(@Valid @RequestBody UserProfileUpdateDto dto) {
        UserGetDto response = userService.updateProfile(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody UserDeleteDto dto) {
        userService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
