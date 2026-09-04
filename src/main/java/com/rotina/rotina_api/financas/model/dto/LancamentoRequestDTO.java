package com.rotina.rotina_api.financas.model.dto;

import com.rotina.rotina_api.financas.model.TipoLancamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequestDTO(

        @NotNull(message = "Categoria é obrigatória.")
        Long categoriaId,

        @NotBlank(message = "Descrição é obrigatória.")
        String descricao,

        @NotNull(message = "Valor é obrigatório.")
        @Positive(message = "Valor precisa ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "Tipo é obrigatório.")
        TipoLancamento tipo,

        @NotNull(message = "Data é obrigatória.")
        LocalDate data
) {
}
