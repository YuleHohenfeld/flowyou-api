package com.rotina.rotina_api.estudo.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.estudo.model.dto.PastaRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.PastaResponseDTO;
import com.rotina.rotina_api.estudo.model.mapper.PastaMapper;
import com.rotina.rotina_api.estudo.service.PastaService;
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
@RequestMapping("/pastas")
@RequiredArgsConstructor
public class PastaController {

    private final PastaService pastaService;
    private final PastaMapper pastaMapper;

    @PostMapping
    public ResponseEntity<PastaResponseDTO> cadastrar(@Valid @RequestBody PastaRequestDTO dto,
                                                        @AuthenticationPrincipal UsuarioPrincipal principal) {
        var pasta = pastaMapper.toEntity(dto, principal.getUsuarioId());
        var pastaSalva = pastaService.cadastrar(pasta);
        return ResponseEntity.status(HttpStatus.CREATED).body(pastaMapper.toResponseDTO(pastaSalva));
    }

    @GetMapping
    public List<PastaResponseDTO> listarRaizes(@AuthenticationPrincipal UsuarioPrincipal principal) {
        return pastaService.listarRaizes(principal.getUsuarioId()).stream()
                .map(pastaMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}/subpastas")
    public List<PastaResponseDTO> listarSubpastas(@PathVariable Long id,
                                                    @AuthenticationPrincipal UsuarioPrincipal principal) {
        return pastaService.listarSubpastas(principal.getUsuarioId(), id).stream()
                .map(pastaMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PastaResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        var pasta = pastaService.buscarPorId(id, principal.getUsuarioId());
        return pastaMapper.toResponseDTO(pasta);
    }

    @PutMapping("/{id}")
    public PastaResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody PastaRequestDTO dto,
                                        @AuthenticationPrincipal UsuarioPrincipal principal) {
        var pastaAtualizada = pastaService.atualizar(id, principal.getUsuarioId(), dto);
        return pastaMapper.toResponseDTO(pastaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        pastaService.excluir(id, principal.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
