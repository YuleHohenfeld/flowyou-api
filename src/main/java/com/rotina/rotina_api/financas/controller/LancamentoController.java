package com.rotina.rotina_api.financas.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.financas.model.Lancamento;
import com.rotina.rotina_api.financas.model.dto.LancamentoRequestDTO;
import com.rotina.rotina_api.financas.model.dto.LancamentoResponseDTO;
import com.rotina.rotina_api.financas.model.mapper.LancamentoMapper;
import com.rotina.rotina_api.financas.service.CategoriaService;
import com.rotina.rotina_api.financas.service.LancamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/financas/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final CategoriaService categoriaService;
    private final LancamentoMapper lancamentoMapper;

    @PostMapping
    public ResponseEntity<LancamentoResponseDTO> criar(@Valid @RequestBody LancamentoRequestDTO dto,
                                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        categoriaService.buscarPorId(dto.categoriaId(), principal.getUsuarioId());

        var lancamento = lancamentoMapper.toEntity(dto, principal.getUsuarioId());
        var lancamentoSalvo = lancamentoService.criar(lancamento);

        return ResponseEntity.status(HttpStatus.CREATED).body(montarResponse(lancamentoSalvo, principal.getUsuarioId()));
    }

    @GetMapping
    public List<LancamentoResponseDTO> listar(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        var lancamentos = (inicio != null && fim != null)
                ? lancamentoService.listarPorPeriodo(principal.getUsuarioId(), inicio, fim)
                : lancamentoService.listar(principal.getUsuarioId());

        return lancamentos.stream()
                .map(lancamento -> montarResponse(lancamento, principal.getUsuarioId()))
                .toList();
    }

    @GetMapping("/{id}")
    public LancamentoResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var lancamento = lancamentoService.buscarPorId(id, principal.getUsuarioId());
        return montarResponse(lancamento, principal.getUsuarioId());
    }

    @PutMapping("/{id}")
    public LancamentoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody LancamentoRequestDTO dto,
                                             @AuthenticationPrincipal UsuarioPrincipal principal) {
        categoriaService.buscarPorId(dto.categoriaId(), principal.getUsuarioId());

        var lancamentoAtualizado = lancamentoService.atualizar(id, principal.getUsuarioId(), dto);
        return montarResponse(lancamentoAtualizado, principal.getUsuarioId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        lancamentoService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }

    private LancamentoResponseDTO montarResponse(Lancamento lancamento, Long usuarioId) {
        var categoria = categoriaService.buscarPorId(lancamento.getCategoriaId(), usuarioId);
        return lancamentoMapper.toResponseDTO(lancamento, categoria.getNome());
    }
}
