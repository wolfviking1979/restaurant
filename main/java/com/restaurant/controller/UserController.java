package com.restaurant.controller;

import com.restaurant.DTO.CommonDTO;
import com.restaurant.DTO.UserDTO;
import com.restaurant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<UserDTO.UserResponse>> createUser(
            @Valid @RequestBody UserDTO.CreateUserRequest request) {
        try {
            UserDTO.UserResponse userResponse = userService.createUser(request);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("User created successfully", userResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<List<UserDTO.UserResponse>>> getAllUsers() {
        List<UserDTO.UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<UserDTO.UserResponse>> getUserById(@PathVariable Long id) {
        try {
            UserDTO.UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<UserDTO.UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO.CreateUserRequest request) {
        try {
            UserDTO.UserResponse userResponse = userService.updateUser(id, request);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("User updated successfully", userResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("User deactivated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> activateUser(@PathVariable Long id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("User activated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }
}