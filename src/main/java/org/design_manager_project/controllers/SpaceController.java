package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.space.SpaceDTO;
<<<<<<< HEAD
import org.design_manager_project.filters.ProjectFilter;
=======
>>>>>>> dea9dbf8a6b7c5571c7fb46fcc99091044abf573
import org.design_manager_project.filters.SpaceFilter;
import org.design_manager_project.models.entity.Space;
import org.design_manager_project.services.ProjectService;
import org.design_manager_project.services.SpaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController extends BaseController<Space, SpaceDTO, SpaceFilter, UUID>{
    private final SpaceService spaceService;
    private final ProjectService projectService;
    protected SpaceController(SpaceService spaceService, ProjectService projectService) {
        super(spaceService);
        this.spaceService = spaceService;
        this.projectService = projectService;
    }
    @GetMapping("/{id}/projects")
    public ApiResponse getProjectWithSpace(
            ProjectFilter filter,
            @PathVariable("id") UUID id
    ){
        return ApiResponse.success(projectService.findAllProjectsWithSpace(filter, id));
    }

}
