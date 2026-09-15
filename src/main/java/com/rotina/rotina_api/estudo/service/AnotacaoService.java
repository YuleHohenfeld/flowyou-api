package com.rotina.rotina_api.estudo.service;

import com.rotina.rotina_api.estudo.model.Anotacao;
import com.rotina.rotina_api.estudo.model.dto.AnotacaoRequestDTO;
import com.rotina.rotina_api.estudo.model.mapper.AnotacaoMapper;
import com.rotina.rotina_api.estudo.repository.AnotacaoRepository;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnotacaoService {

    private final AnotacaoRepository anotacaoRepository;
    private final AnotacaoMapper anotacaoMapper;
    private final PastaService pastaService;

    @Transactional
    public Anotacao cadastrar(Anotacao anotacao) {
        validarPasta(anotacao.getUsuarioId(), anotacao.getPastaId());
        return anotacaoRepository.save(anotacao);
    }

    public List<Anotacao> listarPorPasta(Long usuarioId, Long pastaId) {
        pastaService.buscarPorId(pastaId, usuarioId);
        return anotacaoRepository.findByUsuarioIdAndPastaId(usuarioId, pastaId);
    }

    public List<Anotacao> listarSoltas(Long usuarioId) {
        return anotacaoRepository.findByUsuarioIdAndPastaIdIsNull(usuarioId);
    }

    public Anotacao buscarPorId(Long id, Long usuarioId) {
        return anotacaoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada."));
    }

    @Transactional
    public Anotacao atualizar(Long id, Long usuarioId, AnotacaoRequestDTO dto) {
        var anotacao = buscarPorId(id, usuarioId);
        validarPasta(usuarioId, dto.pastaId());
        anotacaoMapper.atualizarEntity(dto, anotacao);
        return anotacaoRepository.save(anotacao);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var anotacao = buscarPorId(id, usuarioId);
        anotacaoRepository.delete(anotacao);
    }

    private void validarPasta(Long usuarioId, Long pastaId) {
        if (pastaId != null) {
            pastaService.buscarPorId(pastaId, usuarioId);
        }
    }
}
