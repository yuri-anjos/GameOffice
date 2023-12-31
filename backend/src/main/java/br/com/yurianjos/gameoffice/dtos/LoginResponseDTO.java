package br.com.yurianjos.gameoffice.dtos;

import java.time.Instant;
import java.time.LocalDateTime;

public record LoginResponseDTO(String token, Instant expiresAtInstant, LocalDateTime expiresAtDateTime) {
}
