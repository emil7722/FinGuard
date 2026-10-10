package com.example.Finguard.domain.user;

import com.example.Finguard.domain.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        User createdUser = userService.createUser(request);
        UserResponseDto response = new UserResponseDto(
                createdUser.getId(),
                createdUser.getFullName(),
                createdUser.getEmail(),
                createdUser.getStatus()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getStatus()))
                .toList());
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus()
        );
        return ResponseEntity.ok(response);
    }
}
