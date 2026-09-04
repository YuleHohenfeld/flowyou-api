package com.rotina.rotina_api.financas.service;

import com.rotina.rotina_api.financas.model.Lancamento;
import com.rotina.rotina_api.financas.model.dto.LancamentoRequestDTO;
import com.rotina.rotina_api.financas.model.mapper.LancamentoMapper;
import com.rotina.rotina_api.financas.repository.LancamentoRepository;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final LancamentoMapper lancamentoMapper;

    @Transactional
    public Lancamento criar(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    public List<Lancamento> listar(Long usuarioId) {
        return lancamentoRepository.findByUsuarioId(usuarioId);
    }

    public List<Lancamento> listarPorPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return lancamentoRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim);
    }

    public Lancamento buscarPorId(Long id, Long usuarioId) {
        return lancamentoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento não encontrado."));
    }

    @Transactional
    public Lancamento atualizar(Long id, Long usuarioId, LancamentoRequestDTO dto) {
        var lancamento = buscarPorId(id, usuarioId);
        lancamentoMapper.atualizarEntity(dto, lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var lancamento = buscarPorId(id, usuarioId);
        lancamentoRepository.delete(lancamento);
    }
}
