package org.design_manager_project.controllers;

import org.design_manager_project.dtos.label.LabelDTO;
import org.design_manager_project.filters.LabelFilter;
import org.design_manager_project.models.entity.Label;
import org.design_manager_project.services.LabelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/labels")
public class LabelController extends BaseController<Label, LabelDTO, LabelFilter, UUID> {
    private final LabelService labelService;

    protected LabelController(LabelService labelService) {
        super(labelService);
        this.labelService = labelService;
    }
}
