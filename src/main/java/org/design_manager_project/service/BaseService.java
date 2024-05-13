package org.design_manager_project.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import org.design_manager_project.dto.BaseDTO;
import org.design_manager_project.mapper.BaseMapperImpl;
import org.design_manager_project.model.BaseModel;
import org.design_manager_project.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseService<E extends BaseModel, RQ extends BaseDTO<ID>, RS extends BaseDTO<ID>, ID extends UUID> {

    private final BaseRepository<E, ID> baseRepository;
    private final BaseMapperImpl<E, RQ, RS, ID> baseMapper;

    protected BaseService(BaseRepository<E, ID> baseRepository, BaseMapperImpl<E, RQ, RS, ID> baseMapper) {
        this.baseRepository = baseRepository;
        this.baseMapper = baseMapper;
    }

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

        E e = baseMapper.convertToEntity(request);

        baseRepository.save(e);
        return baseMapper.convertToDTO(e);
    }

    public List<RS> createAll(List<RQ> list){
        List<E> es = baseMapper.convertListToEntity(list);

        return baseMapper.convertListToDTO(baseRepository.saveAll(es));
    }

    @Transactional
    public RS update(ID id, RQ request){

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        E updated = baseMapper.convertToEntity(request);

        updated.setId(id);

        E e = baseMapper.updateEntity(updated, entityRepo);

        baseRepository.save(e);

        return baseMapper.convertToDTO(e);
    }

    @Transactional
    public List<RS> updateAll(List<RQ> rqList){
        List<E> eList = rqList.stream().map(e -> {
            E entityRepo = baseRepository.findById(e.getId()).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + e.getId()));

            E updated = baseMapper.convertToEntity(e);

            updated.setId(e.getId());

            return baseMapper.updateEntity(updated, entityRepo);
        }).toList();

        return baseMapper.convertListToDTO(baseRepository.saveAll(eList));
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
