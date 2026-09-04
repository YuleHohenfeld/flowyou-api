package com.rotina.rotina_api.financas.model.dto;

import java.math.BigDecimal;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        BigDecimal limiteMensal
) {
}
