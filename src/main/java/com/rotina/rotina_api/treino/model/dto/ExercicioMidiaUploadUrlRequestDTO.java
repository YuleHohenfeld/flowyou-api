package com.rotina.rotina_api.treino.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ExercicioMidiaUploadUrlRequestDTO(

        @NotBlank(message = "Nome do arquivo é obrigatório.")
        String nomeArquivo
) {
}
