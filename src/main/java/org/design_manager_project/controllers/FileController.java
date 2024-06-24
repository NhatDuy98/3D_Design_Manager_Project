package org.design_manager_project.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.file.FileDTO;
import org.design_manager_project.dtos.file.request.FileRequestWithID;
import org.design_manager_project.dtos.file.response.FileResponse;
import org.design_manager_project.filters.FileFilter;
import org.design_manager_project.services.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable("id") UUID id
    ){
        try {
            FileFilter filter = fileService.download(id);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filter.getFileName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(filter.getStream()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
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
            @RequestBody List<FileRequestWithID> ids
    ){
        fileService.deleteBulk(ids.stream().map(e -> e.getId()).toList());

        return ResponseEntity.ok(ApiResponse.success());
    }

}
