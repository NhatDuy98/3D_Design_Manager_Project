package org.design_manager_project.controllers;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.ResponseObject;
import org.design_manager_project.filter.BaseFilter;
import org.design_manager_project.models.BaseModel;
import org.design_manager_project.services.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@MappedSuperclass
@Validated
public abstract class BaseController<E extends BaseModel,
        RQ extends BaseDTO<ID>, RS extends BaseDTO<ID>,
        FT extends BaseFilter,
        ID extends UUID> {

    private final BaseService<E, RQ, RS, FT, ID> baseService;

    protected BaseController(BaseService<E, RQ, RS, FT, ID> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getPage(FT ft){
        if (baseService.findAllWithPage(ft).isEmpty()){
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Get all failed")
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }

        return ResponseEntity.ok(ResponseObject.builder()
                        .data(baseService.findAllWithPage(ft))
                        .message("Get all information successfully")
                        .status(HttpStatus.OK)
                    .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable("id") ID id){
        if (id == null){
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Not found id")
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Get information of id: " + id + " successfully")
                        .status(HttpStatus.OK)
                        .data(baseService.findById(id))
                    .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody RQ created){
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Create successfully")
                        .status(HttpStatus.CREATED)
                        .data(baseService.create(created))
                    .build());
    }
    @PostMapping("/create-all")
    public ResponseEntity<ResponseObject> createAll(@RequestBody @Valid List<RQ> createdList){
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Create all successfully")
                        .status(HttpStatus.CREATED)
                        .data(baseService.createAll(createdList))
                    .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(
            @PathVariable("id") ID id,
            @Valid @RequestBody RQ updated
    ){
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Update successfully")
                        .status(HttpStatus.OK)
                        .data(baseService.update(id, updated))
                    .build());
    }

    @PutMapping("/update-all")
    public ResponseEntity<ResponseObject> updateAll(
            @RequestBody @Valid List<RQ> updatedList
    ){
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Update all successfully")
                        .status(HttpStatus.OK)
                        .data(baseService.updateAll(updatedList))
                    .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable("id") ID id){
        baseService.deleteById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Delete successfully")
                        .status(HttpStatus.NO_CONTENT)
                    .build());
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ResponseObject> deleteAll(
            @RequestBody List<RQ> deleteList
    ){
        baseService.deleteAll(deleteList.stream().map(e -> e.getId()).toList());
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Delete all successfully")
                .status(HttpStatus.NO_CONTENT)
                .build());
    }

}
