package com.rotina.rotina_api.financas.model.dto;

import java.math.BigDecimal;

public record PerfilFinanceiroResponseDTO(
        Long id,
        BigDecimal salario,
        BigDecimal limiteCartaoCredito
) {
}
