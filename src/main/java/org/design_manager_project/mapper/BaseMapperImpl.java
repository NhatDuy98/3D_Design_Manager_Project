package org.design_manager_project.mapper;

import org.design_manager_project.dto.BaseDTO;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

public interface BaseMapperImpl<E, RQ extends BaseDTO<ID>, RS extends BaseDTO<ID>, ID extends UUID> extends BaseMapper<E, RS>{

    E convertToEntity(RQ rq);
    List<E> convertListToEntity(List<RQ> rqList);
    E convertToEntity(@MappingTarget E entity, RQ request);
}
