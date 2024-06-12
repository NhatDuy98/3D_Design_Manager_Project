package org.design_manager_project.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.file.FileDTO;
import org.design_manager_project.dtos.file.response.FileResponse;
import org.design_manager_project.filters.FileFilter;
import org.design_manager_project.services.FileService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllFiles(FileFilter filter){
        Page<FileResponse> files = fileService.getAllFiles(filter);

        return ResponseEntity.ok(ApiResponse.success(files));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getFile(
            @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFile(id)));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ApiResponse> downloadFile(
            @PathVariable("id") UUID id,
            FileFilter filter
    ){
        fileService.download(id, filter);

        return ResponseEntity.ok()
                .body(ApiResponse.noContent());
    }

    @PostMapping
    public ResponseEntity<ApiResponse> uploadFile(
            @ModelAttribute @Validated FileDTO dto
    ){
        FileResponse file = fileService.upload(dto);
        return ResponseEntity.ok(ApiResponse.success(file));
    }

    @PostMapping("/upload-bulk")
    public ResponseEntity<ApiResponse> uploadBulk(
            @ModelAttribute @Validated FileDTO dto
    ){
        List<FileResponse> files = fileService.uploadBulk(dto);
        return ResponseEntity.ok(ApiResponse.success(files));
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFile(
            @PathVariable("id")UUID id
    ){
        fileService.delete(id);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @SneakyThrows
    @DeleteMapping("/delete-bulk")
    public ResponseEntity<ApiResponse> deleteBulk(
            @RequestBody List<UUID> ids
    ){
        fileService.deleteBulk(ids);

        return ResponseEntity.ok(ApiResponse.success());
    }

}
