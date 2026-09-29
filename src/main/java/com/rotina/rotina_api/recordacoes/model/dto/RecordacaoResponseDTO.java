package com.rotina.rotina_api.recordacoes.model.dto;

public record RecordacaoResponseDTO(
        Long id,
        String categoria,
        String foto,
        String descricao
) {
}
