package com.rotina.rotina_api.estudo.service;

import com.rotina.rotina_api.estudo.model.Pasta;
import com.rotina.rotina_api.estudo.model.dto.PastaRequestDTO;
import com.rotina.rotina_api.estudo.model.mapper.PastaMapper;
import com.rotina.rotina_api.estudo.repository.PastaRepository;
import com.rotina.rotina_api.shared.exception.ConflitoException;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PastaService {

    private final PastaRepository pastaRepository;
    private final PastaMapper pastaMapper;

    @Transactional
    public Pasta cadastrar(Pasta pasta) {
        validarPastaPai(pasta.getUsuarioId(), pasta.getPastaPaiId());
        return pastaRepository.save(pasta);
    }

    public List<Pasta> listarRaizes(Long usuarioId) {
        return pastaRepository.findByUsuarioIdAndPastaPaiIdIsNull(usuarioId);
    }

    public List<Pasta> listarSubpastas(Long usuarioId, Long pastaPaiId) {
        buscarPorId(pastaPaiId, usuarioId);
        return pastaRepository.findByUsuarioIdAndPastaPaiId(usuarioId, pastaPaiId);
    }

    public Pasta buscarPorId(Long id, Long usuarioId) {
        return pastaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pasta não encontrada."));
    }

    @Transactional
    public Pasta atualizar(Long id, Long usuarioId, PastaRequestDTO dto) {
        var pasta = buscarPorId(id, usuarioId);
        validarPastaPai(usuarioId, dto.pastaPaiId());
        pastaMapper.atualizarEntity(dto, pasta);
        return pastaRepository.save(pasta);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var pasta = buscarPorId(id, usuarioId);
        if (!pastaRepository.findByUsuarioIdAndPastaPaiId(usuarioId, id).isEmpty()) {
            throw new ConflitoException("Não é possível excluir uma pasta que contém subpastas.");
        }
        pastaRepository.delete(pasta);
    }

    private void validarPastaPai(Long usuarioId, Long pastaPaiId) {
        if (pastaPaiId != null) {
            buscarPorId(pastaPaiId, usuarioId);
        }
    }
}
