package com.rotina.rotina_api.estudo.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.estudo.model.dto.AnotacaoRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.AnotacaoResponseDTO;
import com.rotina.rotina_api.estudo.model.mapper.AnotacaoMapper;
import com.rotina.rotina_api.estudo.service.AnotacaoService;
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
@RequestMapping("/anotacoes")
@RequiredArgsConstructor
public class AnotacaoController {

    private final AnotacaoService anotacaoService;
    private final AnotacaoMapper anotacaoMapper;

    @PostMapping
    public ResponseEntity<AnotacaoResponseDTO> cadastrar(@Valid @RequestBody AnotacaoRequestDTO dto,
                                                           @AuthenticationPrincipal UsuarioPrincipal principal) {
        var anotacao = anotacaoMapper.toEntity(dto, principal.getUsuarioId());
        var anotacaoSalva = anotacaoService.cadastrar(anotacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(anotacaoMapper.toResponseDTO(anotacaoSalva));
    }

    @GetMapping
    public List<AnotacaoResponseDTO> listar(@RequestParam(required = false) Long pastaId,
                                             @AuthenticationPrincipal UsuarioPrincipal principal) {
        var anotacoes = pastaId != null
                ? anotacaoService.listarPorPasta(principal.getUsuarioId(), pastaId)
                : anotacaoService.listarSoltas(principal.getUsuarioId());
        return anotacoes.stream()
                .map(anotacaoMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public AnotacaoResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var anotacao = anotacaoService.buscarPorId(id, principal.getUsuarioId());
        return anotacaoMapper.toResponseDTO(anotacao);
    }

    @PutMapping("/{id}")
    public AnotacaoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody AnotacaoRequestDTO dto,
                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        var anotacaoAtualizada = anotacaoService.atualizar(id, principal.getUsuarioId(), dto);
        return anotacaoMapper.toResponseDTO(anotacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        anotacaoService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
