package br.com.yurianjos.gameoffice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateUserRequestDTO(
        @NotBlank
        String username,
        @Email
        @NotBlank
        String email,
        String password,
        String confirmPassword) {
}
