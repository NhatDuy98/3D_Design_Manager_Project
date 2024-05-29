package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.project.ProjectDTO;
import org.design_manager_project.filter.ProjectFilter;
import org.design_manager_project.models.entity.Project;
import org.design_manager_project.services.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController extends BaseController<Project, ProjectDTO, ProjectFilter, UUID> {

    private final ProjectService projectService;

    protected ProjectController(ProjectService projectService) {
        super(projectService);
        this.projectService = projectService;
    }

    @GetMapping("/spaces/{id}")
    public ApiResponse getAllProjectsWithSpace(
            ProjectFilter filter,
            @PathVariable("id") UUID spaceId
    ){
        Page<ProjectDTO> projectDTO = projectService.findAllProjectsWithSpace(filter, spaceId);

        if (projectDTO.isEmpty()){
            List<ProjectDTO> projectDTOS = new ArrayList<>();
            return ApiResponse.success(projectDTOS);
        }
        return ApiResponse.success(projectDTO);
    }
}
