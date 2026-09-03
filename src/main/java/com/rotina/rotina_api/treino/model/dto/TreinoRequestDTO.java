package com.rotina.rotina_api.treino.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TreinoRequestDTO(

        @NotNull(message = "Esporte é obrigatório.")
        Long esporteId,

        @NotBlank(message = "Nome do treino é obrigatório.")
        String nome,

        Integer tempoMinutos,

        Double distanciaKm,

        @Valid
        List<ExercicioRequestDTO> exercicios
) {
}
