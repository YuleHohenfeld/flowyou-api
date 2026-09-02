package com.rotina.rotina_api.treino.controller;

import com.rotina.rotina_api.treino.model.dto.EsporteResponseDTO;
import com.rotina.rotina_api.treino.model.mapper.EsporteMapper;
import com.rotina.rotina_api.treino.service.EsporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/esportes")
@RequiredArgsConstructor
public class EsporteController {

    private final EsporteService esporteService;
    private final EsporteMapper esporteMapper;

    @GetMapping
    public List<EsporteResponseDTO> listar() {
        return esporteService.listar().stream()
                .map(esporteMapper::toResponseDTO)
                .toList();
    }
}
