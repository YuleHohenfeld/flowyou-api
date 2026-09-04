package com.rotina.rotina_api.auth.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(

        @NotBlank(message = "Refresh token é obrigatório.")
        String refreshToken
) {
}
