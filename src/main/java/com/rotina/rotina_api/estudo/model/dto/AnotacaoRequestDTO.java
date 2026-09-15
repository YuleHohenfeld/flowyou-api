package com.rotina.rotina_api.estudo.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AnotacaoRequestDTO(

        @NotBlank(message = "Título da anotação é obrigatório.")
        String titulo,

        String conteudo,

        Long pastaId
) {
}
