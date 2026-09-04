package com.rotina.rotina_api.financas.model.mapper;

import com.rotina.rotina_api.financas.model.Categoria;
import com.rotina.rotina_api.financas.model.dto.CategoriaRequestDTO;
import com.rotina.rotina_api.financas.model.dto.CategoriaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "id", ignore = true)
    Categoria toEntity(CategoriaRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(CategoriaRequestDTO dto, @MappingTarget Categoria categoria);

    CategoriaResponseDTO toResponseDTO(Categoria categoria);
}
