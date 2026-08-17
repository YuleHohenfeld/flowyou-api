package com.rotina.rotina_api.shared.exception;

import org.springframework.http.HttpStatus;

public class ConflitoException extends NegocioException {

    public ConflitoException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
