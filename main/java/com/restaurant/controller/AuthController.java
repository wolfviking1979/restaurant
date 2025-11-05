package com.restaurant.controller;

import com.restaurant.dto.CommonDTO;
import com.restaurant.dto.UserDTO;
import com.restaurant.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<CommonDTO.ApiResponse<UserDTO.AuthResponse>> login(
            @Valid @RequestBody UserDTO.LoginRequest loginRequest) {
        try {
            UserDTO.AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Login successful", authResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        try {
            String username = extractUsernameFromToken(token);
            authService.changePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Password changed successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    private String extractUsernameFromToken(String token) {
        // Реализация извлечения username из JWT токена
        return token.replace("Bearer ", ""); // Упрощенная реализация
    }
}