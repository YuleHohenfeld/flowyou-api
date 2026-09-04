package com.rotina.rotina_api.treino.service;

import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.shared.storage.SupabaseStorageService;
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
    private final SupabaseStorageService supabaseStorageService;

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
    public Exercicio adicionarExercicio(Long treinoId, Long usuarioId, ExercicioRequestDTO dto) {
        buscarPorId(treinoId, usuarioId);
        int ordem = exercicioRepository.countByTreinoId(treinoId) + 1;
        return exercicioRepository.save(exercicioMapper.toEntity(dto, treinoId, ordem));
    }

    @Transactional
    public Exercicio atualizarExercicio(Long treinoId, Long exercicioId, Long usuarioId, ExercicioRequestDTO dto) {
        var exercicio = buscarExercicioDoTreino(treinoId, exercicioId, usuarioId);
        exercicioMapper.atualizarEntity(dto, exercicio);
        return exercicioRepository.save(exercicio);
    }

    @Transactional
    public void excluirExercicio(Long treinoId, Long exercicioId, Long usuarioId) {
        var exercicio = buscarExercicioDoTreino(treinoId, exercicioId, usuarioId);
        exercicioRepository.delete(exercicio);
    }

    @Transactional
    public Exercicio atualizarMidia(Long treinoId, Long exercicioId, Long usuarioId, String midiaPath) {
        var exercicio = buscarExercicioDoTreino(treinoId, exercicioId, usuarioId);
        exercicio.setMidiaPath(midiaPath);
        return exercicioRepository.save(exercicio);
    }

    public SupabaseStorageService.UrlDeUpload gerarUrlDeUploadMidia(Long treinoId, Long exercicioId, Long usuarioId,
                                                                       String nomeArquivo) {
        var exercicio = buscarExercicioDoTreino(treinoId, exercicioId, usuarioId);
        String caminho = "exercicios/%d/%d/%d-%s".formatted(usuarioId, exercicio.getId(), System.currentTimeMillis(), nomeArquivo);
        return supabaseStorageService.gerarUrlDeUpload(caminho);
    }

    private Exercicio buscarExercicioDoTreino(Long treinoId, Long exercicioId, Long usuarioId) {
        buscarPorId(treinoId, usuarioId);
        return exercicioRepository.findByIdAndTreinoId(exercicioId, treinoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado."));
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
