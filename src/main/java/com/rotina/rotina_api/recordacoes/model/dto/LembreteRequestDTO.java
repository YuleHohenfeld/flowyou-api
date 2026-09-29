package com.rotina.rotina_api.recordacoes.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LembreteRequestDTO(

        @NotBlank(message = "Destinatário é obrigatório.")
        String destinatario,

        @NotNull(message = "Data é obrigatória.")
        LocalDate data,

        String descricao,

        String foto,

        String link
) {
}
