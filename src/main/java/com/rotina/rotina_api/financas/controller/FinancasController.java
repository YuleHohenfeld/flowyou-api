package com.rotina.rotina_api.financas.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.financas.model.dto.ResumoFinanceiroResponseDTO;
import com.rotina.rotina_api.financas.service.FinancasService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/financas")
@RequiredArgsConstructor
public class FinancasController {

    private final FinancasService financasService;

    @GetMapping("/resumo")
    public ResumoFinanceiroResponseDTO resumo(@RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        return financasService.gerarResumo(principal.getUsuarioId(), mes);
    }
}
