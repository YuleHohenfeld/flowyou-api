package com.rotina.rotina_api.estudo.model.dto;

public record AnotacaoResponseDTO(
        Long id,
        String titulo,
        String conteudo,
        Long pastaId
) {
}
