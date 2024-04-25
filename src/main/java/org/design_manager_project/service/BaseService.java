package org.design_manager_project.service;

import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@MappedSuperclass
public abstract class BaseService<E,D> {

    protected abstract D convertToDTO(E entity);
    protected abstract Page<D> findAllPageWithSearchDTO(String search, Pageable pageable);
    protected abstract List<D> findAllDTO();
    protected abstract E convertToEntity(D dto);
    protected abstract List<E> findAll();
    protected abstract E findById(String id);
    protected abstract E saveEntity(D modelDTO);
    protected abstract E updateEntity(String id, D modelDTO);
    protected abstract void deleteEntity(String id);
}
