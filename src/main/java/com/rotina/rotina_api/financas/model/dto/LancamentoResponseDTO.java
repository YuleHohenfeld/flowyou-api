package com.rotina.rotina_api.financas.model.dto;

import com.rotina.rotina_api.financas.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponseDTO(
        Long id,
        Long categoriaId,
        String categoriaNome,
        String descricao,
        BigDecimal valor,
        TipoLancamento tipo,
        LocalDate data
) {
}
