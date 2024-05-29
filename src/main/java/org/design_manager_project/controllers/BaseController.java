package org.design_manager_project.controllers;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.filter.BaseFilter;
import org.design_manager_project.models.BaseModel;
import org.design_manager_project.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@MappedSuperclass
@Validated
public abstract class BaseController<E extends BaseModel,
        DTO extends BaseDTO<ID>,
        FT extends BaseFilter,
        ID extends UUID> {

    private final BaseService<E, DTO, FT, ID> baseService;

    protected BaseController(BaseService<E, DTO, FT, ID> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPage(FT ft){
        Page<DTO> dtos = baseService.findAllWithPage(ft);

        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable("id") ID id) {
        return ResponseEntity.ok(ApiResponse.success(baseService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid DTO dto){
        return ResponseEntity.ok(ApiResponse.created(baseService.create(dto)));
    }
    @PostMapping("/create-bulk")
    public ResponseEntity<ApiResponse> createAll(@RequestBody @Valid List<DTO> dtos){
        return ResponseEntity.ok(ApiResponse.created(baseService.createAll(dtos)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") ID id,
            @RequestBody @Valid DTO dto
    ){
        return ResponseEntity.ok(ApiResponse.success(baseService.update(id, dto)));
    }

    @PutMapping("/update-bulk")
    public ResponseEntity<ApiResponse> updateAll(
            @RequestBody @Valid List<DTO> dtos
    ){
        return ResponseEntity.ok(ApiResponse.success(baseService.updateAll(dtos)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") ID id){
        baseService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/delete-bulk")
    public ResponseEntity<ApiResponse> deleteAll(
            @RequestBody List<DTO> dtos
    ){
        baseService.deleteAll(dtos.stream().map(e -> e.getId()).toList());
        return ResponseEntity.ok(ApiResponse.noContent());
    }

}
