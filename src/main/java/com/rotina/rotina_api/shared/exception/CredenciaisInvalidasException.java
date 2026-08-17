package com.rotina.rotina_api.shared.exception;

import org.springframework.http.HttpStatus;

public class CredenciaisInvalidasException extends NegocioException {

    public CredenciaisInvalidasException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
