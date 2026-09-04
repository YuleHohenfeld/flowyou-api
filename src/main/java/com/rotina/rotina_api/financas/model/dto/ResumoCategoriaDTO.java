package com.rotina.rotina_api.financas.model.dto;

import java.math.BigDecimal;

public record ResumoCategoriaDTO(
        Long categoriaId,
        String categoriaNome,
        BigDecimal valorGasto,
        BigDecimal limiteMensal,
        BigDecimal disponivel,
        Double percentualDoTotal
) {
}
