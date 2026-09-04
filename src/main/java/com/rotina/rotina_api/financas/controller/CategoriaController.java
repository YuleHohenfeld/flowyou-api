package com.rotina.rotina_api.financas.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.financas.model.dto.CategoriaRequestDTO;
import com.rotina.rotina_api.financas.model.dto.CategoriaResponseDTO;
import com.rotina.rotina_api.financas.model.mapper.CategoriaMapper;
import com.rotina.rotina_api.financas.service.CategoriaService;
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
@RequestMapping("/financas/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto,
                                                         @AuthenticationPrincipal UsuarioPrincipal principal) {
        var categoria = categoriaMapper.toEntity(dto, principal.getUsuarioId());
        var categoriaSalva = categoriaService.criar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toResponseDTO(categoriaSalva));
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar(@AuthenticationPrincipal UsuarioPrincipal principal) {
        return categoriaService.listar(principal.getUsuarioId()).stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var categoria = categoriaService.buscarPorId(id, principal.getUsuarioId());
        return categoriaMapper.toResponseDTO(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto,
                                            @AuthenticationPrincipal UsuarioPrincipal principal) {
        var categoriaAtualizada = categoriaService.atualizar(id, principal.getUsuarioId(), dto);
        return categoriaMapper.toResponseDTO(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        categoriaService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
