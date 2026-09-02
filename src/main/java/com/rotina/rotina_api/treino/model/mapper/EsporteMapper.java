package com.rotina.rotina_api.treino.model.mapper;

import com.rotina.rotina_api.treino.model.Esporte;
import com.rotina.rotina_api.treino.model.dto.EsporteResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EsporteMapper {

    EsporteResponseDTO toResponseDTO(Esporte esporte);
}
