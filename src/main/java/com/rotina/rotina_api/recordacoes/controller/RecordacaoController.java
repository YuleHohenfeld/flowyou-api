package com.rotina.rotina_api.recordacoes.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoRequestDTO;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoResponseDTO;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoUploadUrlRequestDTO;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoUploadUrlResponseDTO;
import com.rotina.rotina_api.recordacoes.model.mapper.RecordacaoMapper;
import com.rotina.rotina_api.recordacoes.service.RecordacaoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recordacoes")
@RequiredArgsConstructor
public class RecordacaoController {

    private final RecordacaoService recordacaoService;
    private final RecordacaoMapper recordacaoMapper;

    @PostMapping("/upload-url")
    public RecordacaoUploadUrlResponseDTO gerarUrlDeUpload(@Valid @RequestBody RecordacaoUploadUrlRequestDTO dto,
                                                             @AuthenticationPrincipal UsuarioPrincipal principal) {
        var resultado = recordacaoService.gerarUrlDeUpload(principal.getUsuarioId(), dto.nomeArquivo());
        return new RecordacaoUploadUrlResponseDTO(resultado.uploadUrl(), resultado.midiaPath());
    }

    @PostMapping
    public ResponseEntity<RecordacaoResponseDTO> cadastrar(@Valid @RequestBody RecordacaoRequestDTO dto,
                                                             @AuthenticationPrincipal UsuarioPrincipal principal) {
        var recordacao = recordacaoMapper.toEntity(dto, principal.getUsuarioId());
        var recordacaoSalva = recordacaoService.cadastrar(recordacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(recordacaoMapper.toResponseDTO(recordacaoSalva));
    }

    @GetMapping
    public List<RecordacaoResponseDTO> listar(@RequestParam(required = false) String categoria,
                                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        var recordacoes = recordacaoService.listar(principal.getUsuarioId(), categoria);
        return recordacoes.stream()
                .map(recordacaoMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public RecordacaoResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var recordacao = recordacaoService.buscarPorId(id, principal.getUsuarioId());
        return recordacaoMapper.toResponseDTO(recordacao);
    }

    @PutMapping("/{id}")
    public RecordacaoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody RecordacaoRequestDTO dto,
                                            @AuthenticationPrincipal UsuarioPrincipal principal) {
        var recordacaoAtualizada = recordacaoService.atualizar(id, principal.getUsuarioId(), dto);
        return recordacaoMapper.toResponseDTO(recordacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        recordacaoService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
