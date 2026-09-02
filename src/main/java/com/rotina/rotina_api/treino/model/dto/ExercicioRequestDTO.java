package com.rotina.rotina_api.treino.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ExercicioRequestDTO(

        @NotBlank(message = "Nome do exercício é obrigatório.")
        String nome,

        Integer series,

        Integer repeticoes,

        Integer tempoSegundos
) {
}
