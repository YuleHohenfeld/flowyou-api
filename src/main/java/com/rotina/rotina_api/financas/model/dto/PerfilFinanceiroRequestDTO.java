package com.rotina.rotina_api.financas.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PerfilFinanceiroRequestDTO(

        @NotNull(message = "Salário é obrigatório.")
        @DecimalMin(value = "0.0", message = "Salário não pode ser negativo.")
        BigDecimal salario,

        @DecimalMin(value = "0.0", message = "Limite do cartão não pode ser negativo.")
        BigDecimal limiteCartaoCredito
) {
}
