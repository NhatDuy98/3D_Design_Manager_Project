package org.design_manager_project.controllers;

import org.design_manager_project.dtos.project.ProjectDTO;
import org.design_manager_project.filters.ProjectFilter;
import org.design_manager_project.models.entity.Project;
import org.design_manager_project.services.ProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController extends BaseController<Project, ProjectDTO, ProjectFilter, UUID> {

    private final ProjectService projectService;

    protected ProjectController(ProjectService projectService) {
        super(projectService);
        this.projectService = projectService;
    }
}
