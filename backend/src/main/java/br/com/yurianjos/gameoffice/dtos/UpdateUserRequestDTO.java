package br.com.yurianjos.gameoffice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateUserRequestDTO(
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email,
        String password,
        String confirmPassword) {
}
