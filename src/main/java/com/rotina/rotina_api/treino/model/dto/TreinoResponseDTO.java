package com.rotina.rotina_api.treino.model.dto;

import java.util.List;

public record TreinoResponseDTO(
        Long id,
        Long esporteId,
        String esporteNome,
        String nome,
        Integer tempoMinutos,
        Double distanciaKm,
        List<ExercicioResponseDTO> exercicios
) {
}
