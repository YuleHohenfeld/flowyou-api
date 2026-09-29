package com.rotina.rotina_api.recordacoes.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordacaoUploadUrlRequestDTO(

        @NotBlank(message = "Nome do arquivo é obrigatório.")
        String nomeArquivo
) {
}
