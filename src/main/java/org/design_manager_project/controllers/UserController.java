package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.user.UserDTO;
<<<<<<< HEAD
import org.design_manager_project.filters.ProjectFilter;
import org.design_manager_project.filters.SpaceFilter;
=======
>>>>>>> dea9dbf8a6b7c5571c7fb46fcc99091044abf573
import org.design_manager_project.filters.UserFilter;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.services.ProjectService;
import org.design_manager_project.services.SpaceService;
import org.design_manager_project.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UserDTO, UserFilter, UUID>{
    private final UserService userService;
    private final SpaceService spaceService;
    private final ProjectService projectService;
    protected UserController(UserService userService, SpaceService spaceService, ProjectService projectService) {
        super(userService);
        this.userService = userService;
        this.spaceService = spaceService;
        this.projectService = projectService;
    }

    @GetMapping("/{id}/spaces")
    public ApiResponse getAllSpacesWithUser(
            SpaceFilter filter,
            @PathVariable("id") UUID userId
    ){
        return ApiResponse.success(spaceService.getAllSpaceWithUserId(filter, userId));
    }

    @GetMapping("/{id}/projects")
    public ApiResponse getAllProjectsWithUser(
            ProjectFilter filter,
            @PathVariable("id") UUID userId
    ){
        return ApiResponse.success(projectService.findAllProjectsWithUser(filter, userId));
    }

}
