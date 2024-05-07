package org.design_manager_project.mapper;

import org.design_manager_project.dto.BaseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaseMapper<E, RQ, RS> {

    RS convertToDTO(E entity);

    E convertToEntity(RQ request);

    Page<RS> convertPageToDTO(Page<E> pageE);

    List<RS> convertListToDTO(List<E> listE);

    List<E> convertListToEntity(List<RQ> rqList);

    Optional<RS> convertOptional(Optional<E> optionalE);

}
