package com.rotina.rotina_api.treino.service;

import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.treino.model.Esporte;
import com.rotina.rotina_api.treino.repository.EsporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EsporteService {

    private final EsporteRepository esporteRepository;

    public List<Esporte> listar() {
        return esporteRepository.findAll();
    }

    public Esporte buscarPorId(Long id) {
        return esporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Esporte não encontrado."));
    }
}
