package org.design_manager_project.service;

import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.mapper.BaseMapper;
import org.design_manager_project.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@MappedSuperclass
@RequiredArgsConstructor
public abstract class BaseService<E, DTO, ID extends String> {

    private final BaseRepository<E, ID> baseRepository;
    private final BaseMapper<E, DTO> baseMapper;

    public List<E> findAll(){
        return baseRepository.findAll();
    }

    public List<DTO> findAllDTO(){
        return baseMapper.convertListToDTO(findAll());
    }

    public Page<E> findAllWithPage(Pageable pageable){
        return baseRepository.findAll(pageable);
    }

    public Page<DTO> findAllWithPageDTO(Pageable pageable){
        return baseMapper.convertPageToDTO(findAllWithPage(pageable));
    }

    public E findById(ID id){
        return baseRepository.findById(id).orElse(null);
    }

    public DTO findByIdDTO(ID id){
        return baseMapper.convertToDTO(findById(id));
    }

    public Optional<E> findAllById(ID id){
        return baseRepository.findById(id);
    }

    @Transactional
    public DTO saveEntity(DTO dto){
        baseRepository.save(baseMapper.convertToEntity(dto));
        return dto;
    }

    @Transactional
    public DTO updateEntity(ID id, DTO updated){
        E e = findById(id);

        E updatedE = baseMapper.convertToEntity(updated);

        baseRepository.save(e);

        return updated;
    }

    @Transactional
    public void deleteById(ID id){
        baseRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(List<ID> ids){
        ids.forEach(id -> {
            baseRepository.deleteById(id);
        });
    }

}
