package org.design_manager_project.mapper;

import org.design_manager_project.dto.BaseDTO;
import org.design_manager_project.model.BaseModel;
import org.design_manager_project.util.AppUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseMapperImpl<E extends BaseModel,
        RQ extends BaseDTO<ID>,
        RS extends BaseDTO<ID>,
        ID extends UUID> implements BaseMapper<E, RQ, RS>{

    protected abstract Class<E> getEntityClass();
    protected abstract Class<RS> getResponseClass();
    @Override
    public RS convertToDTO(E entity) {
        return AppUtils.mapper.map(entity, getResponseClass());
    }
    @Override
    public E convertToEntity(RQ request) {
        return AppUtils.mapper.map(request, getEntityClass());
    }

    @Override
    public List<RS> convertListToDTO(List<E> listE) {
        return listE.stream().map(e -> convertToDTO(e)).toList();
    }

    @Override
    public Page<RS> convertPageToDTO(Page<E> pageE) {
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    public List<E> convertListToEntity(List<RQ> rqList) {
        return rqList.stream().map(e -> convertToEntity(e)).toList();
    }

    @Override
    public Optional<RS> convertOptional(Optional<E> optionalE) {
        return optionalE.map(e -> convertToDTO(e));
    }


    public void updateEntity(E updatedE, E entity) {
        AppUtils.mapper.map(updatedE, entity);
    }
}
