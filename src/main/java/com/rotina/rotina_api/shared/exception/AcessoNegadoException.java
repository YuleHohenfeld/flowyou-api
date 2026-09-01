package com.rotina.rotina_api.shared.exception;

import org.springframework.http.HttpStatus;

public class AcessoNegadoException extends NegocioException {

    public AcessoNegadoException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
