package br.com.yurianjos.gameoffice.dtos.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends Exception {
    private int statusCode;

    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
