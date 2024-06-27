package org.design_manager_project.configurations;

import lombok.RequiredArgsConstructor;
import org.design_manager_project.services.JwtService;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.design_manager_project.utils.Constants.HEADER_STRING;
import static org.design_manager_project.utils.Constants.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class WebSocketTokenFilter implements ChannelInterceptor {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(
            @NotNull Message<?> message,
            @NotNull MessageChannel channel
    ) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(Objects.requireNonNull(accessor).getCommand())){
            String token = accessor.getFirstNativeHeader(HEADER_STRING);
            if (token != null && token.startsWith(TOKEN_PREFIX)){
                String jwt = Objects.requireNonNull(token).substring(7);
                String userEmail = jwtService.extractUserName(jwt);

                if (userEmail != null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    if (jwtService.isTokenValid(jwt, userDetails)){
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        accessor.setUser(authToken);
                    }
                }
            }
        }
        return message;
    }
}