package com.rotina.rotina_api.treino.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.treino.model.dto.ExercicioMidiaRequestDTO;
import com.rotina.rotina_api.treino.model.dto.ExercicioMidiaUploadUrlRequestDTO;
import com.rotina.rotina_api.treino.model.dto.ExercicioMidiaUploadUrlResponseDTO;
import com.rotina.rotina_api.treino.model.dto.ExercicioRequestDTO;
import com.rotina.rotina_api.treino.model.dto.ExercicioResponseDTO;
import com.rotina.rotina_api.treino.model.mapper.ExercicioMapper;
import com.rotina.rotina_api.treino.service.TreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/treinos/{treinoId}/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final TreinoService treinoService;
    private final ExercicioMapper exercicioMapper;

    @PutMapping("/{id}")
    public ExercicioResponseDTO atualizar(@PathVariable Long treinoId, @PathVariable Long id,
                                            @Valid @RequestBody ExercicioRequestDTO dto,
                                            @AuthenticationPrincipal UsuarioPrincipal principal) {
        var exercicio = treinoService.atualizarExercicio(treinoId, id, principal.getUsuarioId(), dto);
        return exercicioMapper.toResponseDTO(exercicio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long treinoId, @PathVariable Long id,
                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        treinoService.excluirExercicio(treinoId, id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/midia")
    public ExercicioResponseDTO atualizarMidia(@PathVariable Long treinoId, @PathVariable Long id,
                                                 @RequestBody ExercicioMidiaRequestDTO dto,
                                                 @AuthenticationPrincipal UsuarioPrincipal principal) {
        var exercicio = treinoService.atualizarMidia(treinoId, id, principal.getUsuarioId(), dto.midiaPath());
        return exercicioMapper.toResponseDTO(exercicio);
    }

    @PostMapping("/{id}/midia/upload-url")
    public ExercicioMidiaUploadUrlResponseDTO gerarUrlDeUploadMidia(@PathVariable Long treinoId, @PathVariable Long id,
                                                                       @Valid @RequestBody ExercicioMidiaUploadUrlRequestDTO dto,
                                                                       @AuthenticationPrincipal UsuarioPrincipal principal) {
        var resultado = treinoService.gerarUrlDeUploadMidia(treinoId, id, principal.getUsuarioId(), dto.nomeArquivo());
        return new ExercicioMidiaUploadUrlResponseDTO(resultado.uploadUrl(), resultado.midiaPath());
    }
}
