package com.rotina.rotina_api.treino.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.treino.model.Treino;
import com.rotina.rotina_api.treino.model.dto.TreinoRequestDTO;
import com.rotina.rotina_api.treino.model.dto.TreinoResponseDTO;
import com.rotina.rotina_api.treino.model.mapper.ExercicioMapper;
import com.rotina.rotina_api.treino.model.mapper.TreinoMapper;
import com.rotina.rotina_api.treino.service.EsporteService;
import com.rotina.rotina_api.treino.service.TreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/treinos")
@RequiredArgsConstructor
public class TreinoController {

    private final TreinoService treinoService;
    private final EsporteService esporteService;
    private final TreinoMapper treinoMapper;
    private final ExercicioMapper exercicioMapper;

    @PostMapping
    public ResponseEntity<TreinoResponseDTO> cadastrar(@Valid @RequestBody TreinoRequestDTO dto,
                                                         @AuthenticationPrincipal UsuarioPrincipal principal) {
        esporteService.buscarPorId(dto.esporteId());

        var treino = treinoMapper.toEntity(dto, principal.getUsuarioId());
        var treinoSalvo = treinoService.cadastrar(treino, dto.exercicios());

        return ResponseEntity.status(HttpStatus.CREATED).body(montarResponse(treinoSalvo));
    }

    @GetMapping
    public List<TreinoResponseDTO> listar(@AuthenticationPrincipal UsuarioPrincipal principal) {
        return treinoService.listar(principal.getUsuarioId()).stream()
                .map(this::montarResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TreinoResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var treino = treinoService.buscarPorId(id, principal.getUsuarioId());
        return montarResponse(treino);
    }

    @PutMapping("/{id}")
    public TreinoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody TreinoRequestDTO dto,
                                         @AuthenticationPrincipal UsuarioPrincipal principal) {
        esporteService.buscarPorId(dto.esporteId());

        var treinoAtualizado = treinoService.atualizar(id, principal.getUsuarioId(), dto);
        return montarResponse(treinoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        treinoService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }

    private TreinoResponseDTO montarResponse(Treino treino) {
        var esporte = esporteService.buscarPorId(treino.getEsporteId());
        var exercicios = treinoService.listarExercicios(treino.getId()).stream()
                .map(exercicioMapper::toResponseDTO)
                .toList();
        return treinoMapper.toResponseDTO(treino, esporte.getNome(), exercicios);
    }
}
