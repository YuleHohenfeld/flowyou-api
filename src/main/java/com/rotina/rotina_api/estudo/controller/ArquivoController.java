package com.rotina.rotina_api.estudo.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.estudo.model.dto.ArquivoRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.ArquivoResponseDTO;
import com.rotina.rotina_api.estudo.model.dto.ArquivoUploadUrlRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.ArquivoUploadUrlResponseDTO;
import com.rotina.rotina_api.estudo.model.mapper.ArquivoMapper;
import com.rotina.rotina_api.estudo.service.ArquivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;
    private final ArquivoMapper arquivoMapper;

    @PostMapping("/upload-url")
    public ArquivoUploadUrlResponseDTO gerarUrlDeUpload(@Valid @RequestBody ArquivoUploadUrlRequestDTO dto,
                                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        var resultado = arquivoService.gerarUrlDeUpload(principal.getUsuarioId(), dto.nomeArquivo());
        return new ArquivoUploadUrlResponseDTO(resultado.uploadUrl(), resultado.midiaPath());
    }

    @PostMapping
    public ResponseEntity<ArquivoResponseDTO> cadastrar(@Valid @RequestBody ArquivoRequestDTO dto,
                                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        var arquivo = arquivoMapper.toEntity(dto, principal.getUsuarioId());
        var arquivoSalvo = arquivoService.cadastrar(arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(arquivoMapper.toResponseDTO(arquivoSalvo));
    }

    @GetMapping
    public List<ArquivoResponseDTO> listar(@RequestParam(required = false) Long pastaId,
                                            @AuthenticationPrincipal UsuarioPrincipal principal) {
        var arquivos = pastaId != null
                ? arquivoService.listarPorPasta(principal.getUsuarioId(), pastaId)
                : arquivoService.listarSoltos(principal.getUsuarioId());
        return arquivos.stream()
                .map(arquivoMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ArquivoResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var arquivo = arquivoService.buscarPorId(id, principal.getUsuarioId());
        return arquivoMapper.toResponseDTO(arquivo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        arquivoService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
