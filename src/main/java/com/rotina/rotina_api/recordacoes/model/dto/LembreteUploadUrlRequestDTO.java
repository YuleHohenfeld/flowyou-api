package com.rotina.rotina_api.recordacoes.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LembreteUploadUrlRequestDTO(

        @NotBlank(message = "Nome do arquivo é obrigatório.")
        String nomeArquivo
) {
}
