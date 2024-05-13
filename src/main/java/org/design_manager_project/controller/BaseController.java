package org.design_manager_project.controller;

import lombok.RequiredArgsConstructor;
import org.design_manager_project.dto.BaseDTO;
import org.design_manager_project.model.BaseModel;
import org.design_manager_project.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class BaseController<E extends BaseModel,
        RQ extends BaseDTO<ID>,
        RS extends BaseDTO<ID>,
        ID extends UUID> {

    private final BaseService<E, RQ, RS, ID> baseService;

    @GetMapping
    public ResponseEntity<Page<RS>> getPage(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(baseService.findAllWithPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RS>> findById(@PathVariable("id") ID id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(baseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RS> create(@RequestBody RQ created){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(baseService.create(created));
    }
    @PostMapping("/create-all")
    public ResponseEntity<List<RS>> createAll(@RequestBody List<RQ> createdList){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(baseService.createAll(createdList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RS> update(
            @PathVariable("id") ID id,
            @RequestBody RQ updated
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(baseService.update(id, updated));
    }

    @PutMapping("/update-all")
    public ResponseEntity<List<RS>> updateAll(
            @RequestBody List<RQ> updatedList
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(baseService.updateAll(updatedList));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") ID id){
        baseService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Deleted");
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAll(
            @RequestBody List<BaseDTO<ID>> deleteList
    ){
        baseService.deleteAll(deleteList.stream().map(e -> e.getId()).toList());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Deleted");
    }

}
