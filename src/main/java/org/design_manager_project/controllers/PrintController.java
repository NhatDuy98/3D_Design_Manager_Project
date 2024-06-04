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

    @DeleteMapping("/{printID}/versions/{versionID}")
    public ResponseEntity<ApiResponse> deleteVersion(
            @PathVariable("printID") UUID printID,
            @PathVariable("versionID") UUID versionID
    ){
        printService.deleteVersion(printID, versionID);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/{printID}/versions/delete-bulk")
    public ResponseEntity<ApiResponse> deleteVersionBulk(
            @PathVariable("printID") UUID printID,
            @RequestBody List<VersionDTO> dto
    ){
        printService.deleteVersionBulk(printID, dto);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

}
