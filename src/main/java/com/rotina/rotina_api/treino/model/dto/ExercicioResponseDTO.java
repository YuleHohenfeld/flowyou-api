package com.rotina.rotina_api.treino.model.dto;

public record ExercicioResponseDTO(
        Long id,
        String nome,
        Integer series,
        String repeticoes,
        Integer tempoSegundos,
        String midiaPath,
        Integer ordem
) {
}
