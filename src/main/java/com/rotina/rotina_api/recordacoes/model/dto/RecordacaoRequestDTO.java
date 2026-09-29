package com.rotina.rotina_api.recordacoes.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordacaoRequestDTO(

        @NotBlank(message = "Categoria é obrigatória.")
        String categoria,

        @NotBlank(message = "Foto é obrigatória.")
        String foto,

        String descricao
) {
}
