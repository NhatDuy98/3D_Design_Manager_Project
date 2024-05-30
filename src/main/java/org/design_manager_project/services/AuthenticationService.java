package org.design_manager_project.services;

import lombok.RequiredArgsConstructor;
import org.design_manager_project.dtos.AuthenticationResponse;
import org.design_manager_project.dtos.user.request.UserRequestForLogin;
import org.design_manager_project.dtos.user.request.UserRequestForRegister;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.models.enums.AuthRole;
import org.design_manager_project.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    public AuthenticationResponse register(UserRequestForRegister userRequestForRegister){
        userService.validateEmailCreate(userRequestForRegister.getEmail());
        User user = userMapper.convertForRegister(userRequestForRegister);
        user.setRole(AuthRole.USER);
        user.setPassword(passwordEncoder.encode(userRequestForRegister.getPassword()));

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse userDTO = userMapper.convertForAuth(user);
        userDTO.setToken(jwtToken);
        return userDTO;
    }

    public AuthenticationResponse login(UserRequestForLogin userRequestForLogin){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestForLogin.getEmail(),
                        userRequestForLogin.getPassword()
                )
        );
        User user = userRepository.findUserByEmail(userRequestForLogin.getEmail());
        AuthenticationResponse userDTO = userMapper.convertForAuth(user);
        String jwtToken = jwtService.generateToken(user);
        userDTO.setToken(jwtToken);
        return userDTO;
    }
}
