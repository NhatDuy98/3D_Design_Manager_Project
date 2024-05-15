package org.design_manager_project.mappers;

import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaseMapper<E, DTO> {

    DTO convertToDTO(E entity);

    Page<DTO> convertPageToDTO(Page<E> pageE);

    List<DTO> convertListToDTO(List<E> listE);

    Optional<DTO> convertOptional(Optional<E> optionalE);

    E updateEntity(E updated,@MappingTarget E entity);

}
