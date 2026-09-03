package com.rotina.rotina_api.treino.model.mapper;

import com.rotina.rotina_api.treino.model.Treino;
import com.rotina.rotina_api.treino.model.dto.ExercicioResponseDTO;
import com.rotina.rotina_api.treino.model.dto.TreinoRequestDTO;
import com.rotina.rotina_api.treino.model.dto.TreinoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TreinoMapper {

    @Mapping(target = "id", ignore = true)
    Treino toEntity(TreinoRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(TreinoRequestDTO dto, @MappingTarget Treino treino);

    TreinoResponseDTO toResponseDTO(Treino treino, String esporteNome, List<ExercicioResponseDTO> exercicios);
}
