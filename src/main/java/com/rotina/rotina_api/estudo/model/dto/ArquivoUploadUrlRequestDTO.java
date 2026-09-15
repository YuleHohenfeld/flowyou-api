package com.rotina.rotina_api.estudo.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ArquivoUploadUrlRequestDTO(

        @NotBlank(message = "Nome do arquivo é obrigatório.")
        String nomeArquivo
) {
}
