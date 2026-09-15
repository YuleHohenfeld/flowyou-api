package com.rotina.rotina_api.estudo.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ArquivoRequestDTO(

        @NotBlank(message = "Nome do arquivo é obrigatório.")
        String nome,

        @NotBlank(message = "Caminho do arquivo é obrigatório.")
        String caminho,

        Long pastaId
) {
}
