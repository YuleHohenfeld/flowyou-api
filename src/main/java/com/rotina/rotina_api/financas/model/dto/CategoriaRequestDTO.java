package com.rotina.rotina_api.financas.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CategoriaRequestDTO(

        @NotBlank(message = "Nome da categoria é obrigatório.")
        String nome,

        @DecimalMin(value = "0.0", message = "Limite mensal não pode ser negativo.")
        BigDecimal limiteMensal
) {
}
