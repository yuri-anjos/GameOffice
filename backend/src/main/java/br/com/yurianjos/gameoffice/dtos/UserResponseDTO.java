package br.com.yurianjos.gameoffice.dtos;

import br.com.yurianjos.gameoffice.domain.User;

import java.util.List;

public record UserResponseDTO(Long id, String email, String username, String role, List<Long> gamesWished) {
    public UserResponseDTO(User user, List<Long> gamesWished) {
        this(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                gamesWished
        );
    }
}
