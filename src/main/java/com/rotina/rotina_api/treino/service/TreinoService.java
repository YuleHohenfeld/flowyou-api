package com.rotina.rotina_api.treino.service;

import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.treino.model.Exercicio;
import com.rotina.rotina_api.treino.model.Treino;
import com.rotina.rotina_api.treino.model.dto.ExercicioRequestDTO;
import com.rotina.rotina_api.treino.model.mapper.ExercicioMapper;
import com.rotina.rotina_api.treino.repository.ExercicioRepository;
import com.rotina.rotina_api.treino.repository.TreinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;
    private final ExercicioMapper exercicioMapper;

    @Transactional
    public Treino cadastrar(Treino treino, List<ExercicioRequestDTO> exerciciosDto) {
        var treinoSalvo = treinoRepository.save(treino);

        if (exerciciosDto != null) {
            int ordem = 1;
            for (ExercicioRequestDTO exercicioDto : exerciciosDto) {
                exercicioRepository.save(exercicioMapper.toEntity(exercicioDto, treinoSalvo.getId(), ordem++));
            }
        }

        return treinoSalvo;
    }

    public List<Treino> listar(Long usuarioId) {
        return treinoRepository.findByUsuarioId(usuarioId);
    }

    public Treino buscarPorId(Long id, Long usuarioId) {
        return treinoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado."));
    }

    public List<Exercicio> listarExercicios(Long treinoId) {
        return exercicioRepository.findByTreinoIdOrderByOrdem(treinoId);
    }
}
