package org.design_manager_project.service;

import jakarta.persistence.MappedSuperclass;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@RequiredArgsConstructor
public abstract class BaseService<E, ID extends UUID> {

    private final BaseRepository<E, ID> baseRepository;

    public List<E> findAll(){
        return baseRepository.findAll();
    }

    public Page<E> findAllWithPage(Pageable pageable){
        return baseRepository.findAll(pageable);
    }

    public E findById(ID id){
        return baseRepository.findById(id).orElse(null);
    }

    public Optional<E> findAllById(ID id){
        return baseRepository.findById(id);
    }

    public E saveEntity(E entity){
        return baseRepository.save(entity);
    }

    public E updateEntity(E entity){
        return baseRepository.save(entity);
    }

    public void deleteById(ID id){
        baseRepository.deleteById(id);
    }

    public void deleteAll(List<ID> ids){
        ids.forEach(id -> {
            baseRepository.deleteById(id);
        });
    }

}
