package org.design_manager_project.controller;

import jakarta.persistence.MappedSuperclass;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.mapper.BaseMapper;
import org.design_manager_project.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@MappedSuperclass
@RequiredArgsConstructor
public abstract class BaseController<E, DTO, ID extends String> {

    private final BaseService<E, DTO, ID> baseService;

    @GetMapping("")
    public ResponseEntity<Page<DTO>> getPage(Pageable pageable){
        return new ResponseEntity<>(baseService.findAllWithPageDTO(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> findById(@PathVariable ID id){
        return new ResponseEntity<>(baseService.findByIdDTO(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<DTO> create(@RequestBody DTO created){
        DTO dto = baseService.saveEntity(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> updateAll(
            @PathVariable ID id,
            @RequestBody DTO updated
    ){
        DTO dto = baseService.updateEntity(id, updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable ID id){
        baseService.deleteById(id);
        return new ResponseEntity<>("deleted",HttpStatus.NO_CONTENT);
    }

}
