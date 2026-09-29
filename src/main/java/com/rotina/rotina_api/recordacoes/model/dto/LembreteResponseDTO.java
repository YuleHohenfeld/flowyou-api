package com.rotina.rotina_api.recordacoes.model.dto;

import java.time.LocalDate;

public record LembreteResponseDTO(
        Long id,
        String destinatario,
        LocalDate data,
        String descricao,
        String foto
) {
}
