package org.design_manager_project.mapper;

import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseMapper<E, DTO> {

    DTO convertToDTO(E entity);

    E convertToEntity(DTO dto);

    Page<DTO> convertPageToDTO(Page<E> pageE);

    List<DTO> convertListToDTO(List<E> listE);
}
