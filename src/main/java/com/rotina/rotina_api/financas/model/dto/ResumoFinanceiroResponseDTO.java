package com.rotina.rotina_api.financas.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record ResumoFinanceiroResponseDTO(
        BigDecimal salario,
        BigDecimal totalGanhos,
        BigDecimal totalGasto,
        BigDecimal totalInvestido,
        BigDecimal saldo,
        BigDecimal limiteCartaoCredito,
        List<ResumoCategoriaDTO> categorias
) {
}
