package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.dtos.version.VersionDTO;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.models.entity.Print;
import org.design_manager_project.services.PrintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prints")
public class PrintController extends BaseController<Print, PrintDTO, PrintFilter, UUID> {
    private final PrintService printService;
    protected PrintController(PrintService printService) {
        super(printService);
        this.printService = printService;
    }

    @PutMapping("/{printId}/versions/{versionId}")
    public ResponseEntity<ApiResponse> updateLatestVersion(
            @PathVariable("printId") UUID printId,
            @PathVariable("versionId") UUID versionId
    ){
        PrintDTO printDTO = printService.updateLatestVersion(printId, versionId);

        return ResponseEntity.ok(ApiResponse.success(printDTO));
    }

    @DeleteMapping("/{printId}/versions/{versionId}")
    public ResponseEntity<ApiResponse> deleteVersion(
            @PathVariable("printId") UUID printId,
            @PathVariable("versionId") UUID versionId
    ){
        printService.deleteVersion(printId, versionId);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/{printId}/versions/delete-bulk")
    public ResponseEntity<ApiResponse> deleteVersionBulk(
            @PathVariable("printId") UUID printId,
            @RequestBody List<VersionDTO> dto
    ){
        printService.deleteVersionBulk(printId, dto);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

}
