package com.rotina.rotina_api.treino.model.mapper;

import com.rotina.rotina_api.treino.model.Exercicio;
import com.rotina.rotina_api.treino.model.dto.ExercicioRequestDTO;
import com.rotina.rotina_api.treino.model.dto.ExercicioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExercicioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "midiaPath", ignore = true)
    Exercicio toEntity(ExercicioRequestDTO dto, Long treinoId, Integer ordem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treinoId", ignore = true)
    @Mapping(target = "ordem", ignore = true)
    @Mapping(target = "midiaPath", ignore = true)
    void atualizarEntity(ExercicioRequestDTO dto, @MappingTarget Exercicio exercicio);

    ExercicioResponseDTO toResponseDTO(Exercicio exercicio);
}
