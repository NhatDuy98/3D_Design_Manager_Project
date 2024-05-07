package org.design_manager_project.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.dto.BaseDTO;
import org.design_manager_project.mapper.BaseMapper;
import org.design_manager_project.mapper.BaseMapperImpl;
import org.design_manager_project.model.BaseModel;
import org.design_manager_project.repository.BaseRepository;
import org.design_manager_project.util.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class BaseService<E extends BaseModel,
        RQ extends BaseDTO<ID>,
        RS extends BaseDTO<ID>,
        ID extends UUID> {

    private final BaseRepository<E, ID> baseRepository;
    private final BaseMapperImpl<E, RQ, RS, ID> baseMapper;
    public List<RS> findAll(){
        return baseMapper.convertListToDTO(baseRepository.findAll());
    }

    public Page<RS> findAllWithPage(Pageable pageable){
        return baseMapper.convertPageToDTO(baseRepository.findAll(pageable));
    }

    public Optional<RS> findById(ID id){
        return baseMapper.convertOptional(baseRepository.findById(id));
    }

    public RS create(RQ request){
        E entity = baseMapper.convertToEntity(request);
        baseRepository.save(entity);
        return baseMapper.convertToDTO(entity);
    }

    public List<RS> createAll(List<RQ> list){
        List<E> eList = baseMapper.convertListToEntity(list);

        return baseMapper.convertListToDTO(eList.stream().map(e -> baseRepository.save(e)).toList());
    }

    @Transactional
    public RS update(ID id, RQ request){

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        E entity = baseMapper.convertToEntity(request);

        entity.setId(id);

        baseMapper.updateEntity(entity, entityRepo);

        baseRepository.save(entityRepo);

        return baseMapper.convertToDTO(entityRepo);
    }

    @Transactional
    public List<RS> updateAll(List<RQ> rqList){
        return rqList.stream().map(e -> update(e.getId(), e)).toList();
    }


    @Transactional
    public void deleteById(ID id){
        baseRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(List<ID> ids){
        baseRepository.deleteAllById(ids);
    }

}
