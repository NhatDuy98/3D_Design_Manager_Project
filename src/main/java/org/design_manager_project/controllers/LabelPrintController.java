package org.design_manager_project.controllers;

import org.design_manager_project.dtos.label_print.LabelPrintDTO;
import org.design_manager_project.filters.LabelPrintFilter;
import org.design_manager_project.models.entity.LabelPrint;
import org.design_manager_project.services.LabelPrintService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/label-prints")
public class LabelPrintController extends BaseController<LabelPrint, LabelPrintDTO, LabelPrintFilter, UUID> {
    private final LabelPrintService labelPrintService;
    protected LabelPrintController(LabelPrintService labelPrintService) {
        super(labelPrintService);
        this.labelPrintService = labelPrintService;
    }
}
