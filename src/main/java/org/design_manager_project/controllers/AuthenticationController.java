package org.design_manager_project.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.AuthenticationResponse;
import org.design_manager_project.dtos.user.request.UserRequestForLogin;
import org.design_manager_project.dtos.user.request.UserRequestForRegister;
import org.design_manager_project.services.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/home")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody @Valid UserRequestForRegister userRequestForRegister){
        AuthenticationResponse userDTO = authenticationService.register(userRequestForRegister);

        return ApiResponse.created(userDTO);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid UserRequestForLogin userRequestForLogin){
        AuthenticationResponse authenticationResponse = authenticationService.login(userRequestForLogin);

        return ApiResponse.success(authenticationResponse);
    }
}
