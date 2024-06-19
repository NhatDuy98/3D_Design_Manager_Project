package org.design_manager_project.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.repositories.UserRepository;
import org.design_manager_project.services.OnlOffService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class WebSocketEventListener {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;
    private final OnlOffService onlOffService;

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event){
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Subscribe success - {}", event.getMessage());
        messagingTemplate.convertAndSend(stompAccessor.getDestination(), "hello");
    }
    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent event){
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        UUID projectId = UUID.fromString(Objects.requireNonNull(stompAccessor.getFirstNativeHeader("projectId")));
        onlOffService.addOnlineUser(event.getUser(), projectId);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event){
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        UUID projectId = UUID.fromString(Objects.requireNonNull(stompAccessor.getFirstNativeHeader("projectId")));
        onlOffService.removeOnlineUser(event.getUser(), projectId);
    }
}
