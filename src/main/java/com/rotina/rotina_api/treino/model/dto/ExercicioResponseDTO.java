package com.rotina.rotina_api.treino.model.dto;

public record ExercicioResponseDTO(
        Long id,
        String nome,
        Integer series,
        Integer repeticoes,
        Integer tempoSegundos,
        String midiaPath,
        Integer ordem
) {
}
