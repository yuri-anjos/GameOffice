package br.com.yurianjos.gameoffice.domain;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String role;

    Roles(String role) {
        this.role = role;
    }
}
