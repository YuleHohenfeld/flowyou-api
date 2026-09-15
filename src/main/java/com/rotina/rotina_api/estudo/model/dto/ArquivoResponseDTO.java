package com.rotina.rotina_api.estudo.model.dto;

public record ArquivoResponseDTO(
        Long id,
        String nome,
        String caminho,
        Long pastaId
) {
}
