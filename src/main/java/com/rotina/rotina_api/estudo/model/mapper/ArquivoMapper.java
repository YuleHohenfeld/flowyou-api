package com.rotina.rotina_api.estudo.model.mapper;

import com.rotina.rotina_api.estudo.model.Arquivo;
import com.rotina.rotina_api.estudo.model.dto.ArquivoRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.ArquivoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArquivoMapper {

    @Mapping(target = "id", ignore = true)
    Arquivo toEntity(ArquivoRequestDTO dto, Long usuarioId);

    ArquivoResponseDTO toResponseDTO(Arquivo arquivo);
}
