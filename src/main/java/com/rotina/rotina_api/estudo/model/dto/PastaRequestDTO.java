package com.rotina.rotina_api.estudo.model.dto;

import jakarta.validation.constraints.NotBlank;

public record PastaRequestDTO(

        @NotBlank(message = "Nome da pasta é obrigatório.")
        String nome,

        Long pastaPaiId
) {
}
