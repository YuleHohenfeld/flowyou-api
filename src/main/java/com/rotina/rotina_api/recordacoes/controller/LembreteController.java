package com.rotina.rotina_api.recordacoes.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteRequestDTO;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteResponseDTO;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteUploadUrlRequestDTO;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteUploadUrlResponseDTO;
import com.rotina.rotina_api.recordacoes.model.mapper.LembreteMapper;
import com.rotina.rotina_api.recordacoes.service.LembreteService;
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
@RequestMapping("/lembretes")
@RequiredArgsConstructor
public class LembreteController {

    private final LembreteService lembreteService;
    private final LembreteMapper lembreteMapper;

    @PostMapping("/upload-url")
    public LembreteUploadUrlResponseDTO gerarUrlDeUpload(@Valid @RequestBody LembreteUploadUrlRequestDTO dto,
                                                           @AuthenticationPrincipal UsuarioPrincipal principal) {
        var resultado = lembreteService.gerarUrlDeUpload(principal.getUsuarioId(), dto.nomeArquivo());
        return new LembreteUploadUrlResponseDTO(resultado.uploadUrl(), resultado.midiaPath());
    }

    @PostMapping
    public ResponseEntity<LembreteResponseDTO> cadastrar(@Valid @RequestBody LembreteRequestDTO dto,
                                                           @AuthenticationPrincipal UsuarioPrincipal principal) {
        var lembrete = lembreteMapper.toEntity(dto, principal.getUsuarioId());
        var lembreteSalvo = lembreteService.cadastrar(lembrete);
        return ResponseEntity.status(HttpStatus.CREATED).body(lembreteMapper.toResponseDTO(lembreteSalvo));
    }

    @GetMapping
    public List<LembreteResponseDTO> listar(@AuthenticationPrincipal UsuarioPrincipal principal) {
        var lembretes = lembreteService.listar(principal.getUsuarioId());
        return lembretes.stream()
                .map(lembreteMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public LembreteResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var lembrete = lembreteService.buscarPorId(id, principal.getUsuarioId());
        return lembreteMapper.toResponseDTO(lembrete);
    }

    @PutMapping("/{id}")
    public LembreteResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody LembreteRequestDTO dto,
                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        var lembreteAtualizado = lembreteService.atualizar(id, principal.getUsuarioId(), dto);
        return lembreteMapper.toResponseDTO(lembreteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        lembreteService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
