package org.design_manager_project.mappers;

import org.design_manager_project.dtos.BaseDTO;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

public interface BaseMapperImpl<E, RQ extends BaseDTO<ID>, RS extends BaseDTO<ID>, ID extends UUID> extends BaseMapper<E, RS>{

    E convertToEntityForUpdate(RQ rq);
    E convertToEntityForCreate(RQ rq);
    List<E> convertListToEntityForCreate(List<RQ> rqList);
    E convertToEntity(@MappingTarget E entity, RQ request);
}
