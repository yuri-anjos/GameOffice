package br.com.yurianjos.gameoffice.dtos;

import br.com.yurianjos.gameoffice.domain.User;

public record ComboDTO(Long id, String description) {

    public ComboDTO(User user) {
        this(user.getId(), user.getName());
    }
}
