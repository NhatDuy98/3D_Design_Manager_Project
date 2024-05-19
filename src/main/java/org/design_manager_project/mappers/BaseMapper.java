package org.design_manager_project.mappers;

import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaseMapper<E, DTO> {

    DTO convertToDTO(E entity);

    E convertToEntity(DTO dto);

    Page<DTO> convertPageToDTO(Page<E> pageE);

    List<DTO> convertListToDTO(List<E> listE);

    List<E> convertListToEntity(List<DTO> dtoList);

    Optional<DTO> convertOptional(Optional<E> optionalE);

    E updateEntity(DTO dto,@MappingTarget E entity);

}
