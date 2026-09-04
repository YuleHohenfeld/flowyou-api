package com.rotina.rotina_api.financas.service;

import com.rotina.rotina_api.financas.model.PerfilFinanceiro;
import com.rotina.rotina_api.financas.model.dto.PerfilFinanceiroRequestDTO;
import com.rotina.rotina_api.financas.model.mapper.PerfilFinanceiroMapper;
import com.rotina.rotina_api.financas.repository.PerfilFinanceiroRepository;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerfilFinanceiroService {

    private final PerfilFinanceiroRepository perfilFinanceiroRepository;
    private final PerfilFinanceiroMapper perfilFinanceiroMapper;

    public PerfilFinanceiro buscarPorUsuarioId(Long usuarioId) {
        return perfilFinanceiroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil financeiro não encontrado."));
    }

    @Transactional
    public PerfilFinanceiro salvar(Long usuarioId, PerfilFinanceiroRequestDTO dto) {
        var perfilExistente = perfilFinanceiroRepository.findByUsuarioId(usuarioId);

        if (perfilExistente.isPresent()) {
            var perfil = perfilExistente.get();
            perfilFinanceiroMapper.atualizarEntity(dto, perfil);
            return perfilFinanceiroRepository.save(perfil);
        }

        return perfilFinanceiroRepository.save(perfilFinanceiroMapper.toEntity(dto, usuarioId));
    }
}
