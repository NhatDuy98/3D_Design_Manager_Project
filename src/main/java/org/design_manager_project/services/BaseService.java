package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.BaseFilter;
import org.design_manager_project.mappers.BaseMapper;
import org.design_manager_project.models.BaseModel;
import org.design_manager_project.repositories.BaseRepository;
import org.design_manager_project.utils.Constants;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseService<E extends BaseModel,
        DTO extends BaseDTO<ID>,
        FT extends BaseFilter,
        ID extends UUID> {

    private final BaseRepository<E, FT, ID> baseRepository;
    private final BaseMapper<E, DTO> baseMapper;

    protected BaseService(BaseRepository<E, FT, ID> baseRepository, BaseMapper<E, DTO> baseMapper) {
        this.baseRepository = baseRepository;
        this.baseMapper = baseMapper;
    }
    public List<DTO> findAll(){
        return baseMapper.convertListToDTO(baseRepository.findAll());
    }

    public Page<DTO> findAllWithPage(FT ft){
        return baseMapper.convertPageToDTO(baseRepository.findAllWithFilter(ft.getPageable(), ft));
    }

    public void checkIfDeleted(E e){
        try {
            Field deletedAtField = e.getClass().getDeclaredField("deletedAt");
            deletedAtField.setAccessible(true);
            Object deletedAtValue = deletedAtField.get(e);
            if (deletedAtValue != null) {
                throw new BadRequestException(Constants.OBJECT_DELETED);
            }
        } catch (NoSuchFieldException| IllegalAccessException ex){

        }
    }

    public Optional<DTO> findById(ID id) {
        E e = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        try {
            checkIfDeleted(e);
        } catch (BadRequestException ex){
            throw new BadRequestException(ex.getMessage());
        }

        return baseMapper.convertOptional(Optional.of(e));
    }

    public DTO create(DTO dto){

        E e = baseMapper.convertToEntity(dto);

        baseRepository.save(e);
        return baseMapper.convertToDTO(e);
    }

    public List<DTO> createAll(List<DTO> list){
        List<E> es = baseMapper.convertListToEntity(list);

        return baseMapper.convertListToDTO(baseRepository.saveAll(es));
    }

    @Transactional
    public DTO update(ID id, DTO dto){

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        try {
            checkIfDeleted(entityRepo);
        } catch (BadRequestException ex){
            throw new BadRequestException(ex.getMessage());
        }

        E updated = baseMapper.updateEntity(dto, entityRepo);

        updated.setId(id);

        baseRepository.save(updated);

        return baseMapper.convertToDTO(updated);
    }

    @Transactional
    public List<DTO> updateAll(List<DTO> rqList){
        List<E> eList = rqList.stream().map(e -> {
            E entityRepo = baseRepository.findById(e.getId()).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + e.getId()));
            try {
                checkIfDeleted(entityRepo);
            } catch (BadRequestException ex){
                throw new BadRequestException(ex.getMessage());
            }
            E updated = baseMapper.updateEntity(e, entityRepo);

            updated.setId(e.getId());

            return updated;
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
