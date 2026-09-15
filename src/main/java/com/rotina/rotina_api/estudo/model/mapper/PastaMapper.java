package com.rotina.rotina_api.estudo.model.mapper;

import com.rotina.rotina_api.estudo.model.Pasta;
import com.rotina.rotina_api.estudo.model.dto.PastaRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.PastaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PastaMapper {

    @Mapping(target = "id", ignore = true)
    Pasta toEntity(PastaRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(PastaRequestDTO dto, @MappingTarget Pasta pasta);

    PastaResponseDTO toResponseDTO(Pasta pasta);
}
