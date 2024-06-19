package org.design_manager_project.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.repositories.UserRepository;
import org.design_manager_project.services.OnlOffService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.util.List;
import java.util.Map;
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
    public void handleSubscribeEvent(SessionSubscribeEvent event){
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Subscribe success - {}", event.getMessage());
        messagingTemplate.convertAndSend(stompAccessor.getDestination(), "hello");
    }

    @EventListener
    public void handleUnSubscribeEvent(SessionUnsubscribeEvent event){

    }
    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent event){
        Map<String, List<String>> nativeHeaders = getNativeHeaders(event);

        UUID projectId = UUID.fromString(Objects.requireNonNull(nativeHeaders).get("projectId").get(0));
        onlOffService.addOnlineUser(event.getUser(), projectId);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event){
        Map<String, List<String>> nativeHeaders = getNativeHeaders(event);

        UUID projectId = UUID.fromString(Objects.requireNonNull(nativeHeaders).get("projectId").get(0));
        onlOffService.removeOnlineUser(event.getUser(), projectId);
    }

    private Map<String, List<String>> getNativeHeaders(AbstractSubProtocolEvent event){
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        @SuppressWarnings("rawtypes")
        GenericMessage connectHeader = (GenericMessage) stompAccessor
                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) Objects.requireNonNull(connectHeader).getHeaders()
                .get(NativeMessageHeaderAccessor.NATIVE_HEADERS);

        return nativeHeaders;
    }
}
