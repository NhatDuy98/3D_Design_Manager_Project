package org.design_manager_project.controllers;

import org.design_manager_project.dtos.user.request.UserRequest;
import org.design_manager_project.dtos.user.response.UserResponse;
import org.design_manager_project.filter.UserFilter;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UserRequest, UserResponse, UserFilter, UUID>{

    private final UserService userService;
    protected UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

}
