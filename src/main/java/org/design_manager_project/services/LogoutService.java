package org.design_manager_project.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static org.design_manager_project.utils.Constants.HEADER_STRING;
import static org.design_manager_project.utils.Constants.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(HEADER_STRING);
        final String jwt;
        if (authHeader == null || authHeader.startsWith(TOKEN_PREFIX)){
            return;
        }
        SecurityContextHolder.clearContext();
    }
}
