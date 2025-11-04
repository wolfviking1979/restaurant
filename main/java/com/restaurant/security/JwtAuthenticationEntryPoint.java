package com.restaurant.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.DTO.CommonDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Правильное использование logger
        logger.error("Unauthorized error. Message: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = getErrorMessage(authException);

        CommonDTO.ApiResponse<String> apiResponse = CommonDTO.ApiResponse.error(errorMessage);

        try {
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            logger.error("Error writing response: {}", e.getMessage());
            throw e;
        }
    }

    private String getErrorMessage(AuthenticationException authException) {
        if (authException instanceof BadCredentialsException) {
            return "Invalid username or password";
        } else if (authException instanceof InsufficientAuthenticationException) {
            return "Authentication token is required";
        } else if (authException instanceof AccountExpiredException) {
            return "Account has expired";
        } else if (authException instanceof LockedException) {
            return "Account is locked";
        } else if (authException instanceof DisabledException) {
            return "Account is disabled";
        } else if (authException instanceof CredentialsExpiredException) {
            return "Credentials have expired";
        } else if (authException instanceof AuthenticationCredentialsNotFoundException) {
            return "Authentication credentials not found";
        } else {
            return "Authentication failed: " + authException.getMessage();
        }
    }
}