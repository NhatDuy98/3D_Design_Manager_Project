package org.design_manager_project.controllers;

import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.models.entity.Print;
import org.design_manager_project.services.PrintService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/prints")
public class PrintController extends BaseController<Print, PrintDTO, PrintFilter, UUID> {
    private final PrintService printService;
    protected PrintController(PrintService printService) {
        super(printService);
        this.printService = printService;
    }
}
