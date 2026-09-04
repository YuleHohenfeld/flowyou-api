package com.rotina.rotina_api.financas.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.financas.model.dto.PerfilFinanceiroRequestDTO;
import com.rotina.rotina_api.financas.model.dto.PerfilFinanceiroResponseDTO;
import com.rotina.rotina_api.financas.model.mapper.PerfilFinanceiroMapper;
import com.rotina.rotina_api.financas.service.PerfilFinanceiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financas/perfil")
@RequiredArgsConstructor
public class PerfilFinanceiroController {

    private final PerfilFinanceiroService perfilFinanceiroService;
    private final PerfilFinanceiroMapper perfilFinanceiroMapper;

    @GetMapping
    public PerfilFinanceiroResponseDTO buscar(@AuthenticationPrincipal UsuarioPrincipal principal) {
        var perfil = perfilFinanceiroService.buscarPorUsuarioId(principal.getUsuarioId());
        return perfilFinanceiroMapper.toResponseDTO(perfil);
    }

    @PutMapping
    public PerfilFinanceiroResponseDTO salvar(@Valid @RequestBody PerfilFinanceiroRequestDTO dto,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        var perfil = perfilFinanceiroService.salvar(principal.getUsuarioId(), dto);
        return perfilFinanceiroMapper.toResponseDTO(perfil);
    }
}
