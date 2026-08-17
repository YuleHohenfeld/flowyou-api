package com.rotina.rotina_api.shared.handler;

import com.rotina.rotina_api.shared.exception.ErroResponse;
import com.rotina.rotina_api.shared.exception.NegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroResponse> tratarNegocioException(NegocioException ex, HttpServletRequest request) {
        ErroResponse corpo = new ErroResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(corpo);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErroResponse corpo = new ErroResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                mensagem,
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(corpo);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInesperado(Exception ex, HttpServletRequest request) {
        // Detalhe real do erro deve ir pro log do servidor, não pro cliente.
        ErroResponse corpo = new ErroResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Erro interno inesperado.",
                request.getRequestURI()
        );
        return ResponseEntity.internalServerError().body(corpo);
    }
}
