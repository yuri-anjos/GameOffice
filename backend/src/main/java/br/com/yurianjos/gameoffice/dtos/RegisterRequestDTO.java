package br.com.yurianjos.gameoffice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank
        String username,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String confirmPassword) {
}
