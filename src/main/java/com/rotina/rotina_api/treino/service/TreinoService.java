package com.rotina.rotina_api.treino.service;

import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.treino.model.Exercicio;
import com.rotina.rotina_api.treino.model.Treino;
import com.rotina.rotina_api.treino.model.dto.ExercicioRequestDTO;
import com.rotina.rotina_api.treino.model.dto.TreinoRequestDTO;
import com.rotina.rotina_api.treino.model.mapper.ExercicioMapper;
import com.rotina.rotina_api.treino.model.mapper.TreinoMapper;
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
    private final TreinoMapper treinoMapper;

    @Transactional
    public Treino cadastrar(Treino treino, List<ExercicioRequestDTO> exerciciosDto) {
        var treinoSalvo = treinoRepository.save(treino);
        salvarExercicios(treinoSalvo.getId(), exerciciosDto);
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

    @Transactional
    public Treino atualizar(Long id, Long usuarioId, TreinoRequestDTO dto) {
        var treino = buscarPorId(id, usuarioId);
        treinoMapper.atualizarEntity(dto, treino);
        var treinoAtualizado = treinoRepository.save(treino);

        exercicioRepository.deleteByTreinoId(id);
        salvarExercicios(id, dto.exercicios());

        return treinoAtualizado;
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var treino = buscarPorId(id, usuarioId);
        exercicioRepository.deleteByTreinoId(id);
        treinoRepository.delete(treino);
    }

    @Transactional
    public Exercicio atualizarExercicio(Long treinoId, Long exercicioId, Long usuarioId, ExercicioRequestDTO dto) {
        buscarPorId(treinoId, usuarioId);
        var exercicio = exercicioRepository.findByIdAndTreinoId(exercicioId, treinoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado."));

        exercicioMapper.atualizarEntity(dto, exercicio);
        return exercicioRepository.save(exercicio);
    }

    @Transactional
    public void excluirExercicio(Long treinoId, Long exercicioId, Long usuarioId) {
        buscarPorId(treinoId, usuarioId);
        var exercicio = exercicioRepository.findByIdAndTreinoId(exercicioId, treinoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado."));

        exercicioRepository.delete(exercicio);
    }

    private void salvarExercicios(Long treinoId, List<ExercicioRequestDTO> exerciciosDto) {
        if (exerciciosDto != null) {
            int ordem = 1;
            for (ExercicioRequestDTO exercicioDto : exerciciosDto) {
                exercicioRepository.save(exercicioMapper.toEntity(exercicioDto, treinoId, ordem++));
            }
        }
    }
}
