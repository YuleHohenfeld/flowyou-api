package com.rotina.rotina_api.shared.exception;

import java.time.LocalDateTime;

public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho
) {
}
