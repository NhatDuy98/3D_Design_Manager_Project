package org.design_manager_project.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.dtos.user.response.UserOnlineDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.repositories.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

import static org.design_manager_project.utils.Constants.DATA_NOT_FOUND;

@Component
@RequiredArgsConstructor
@Getter
public class WebSocketEventListener {
    private Set<UserOnlineDTO> onlineUsers;
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;

    @EventListener
    public void handleWebSocketListener(SessionConnectedEvent event){

        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());

        @SuppressWarnings("rawtypes")
        GenericMessage connectHeader = (GenericMessage) stompAccessor
                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);

        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) Objects.requireNonNull(connectHeader).getHeaders()
                .get(NativeMessageHeaderAccessor.NATIVE_HEADERS);

        UUID userId = UUID.fromString(Objects.requireNonNull(nativeHeaders).get("id").get(0));
        String sessionId = stompAccessor.getSessionId();

        if (this.onlineUsers == null){
            this.onlineUsers = new HashSet<>();
        }
        var user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(DATA_NOT_FOUND));

        if (user != null){
            UserOnlineDTO onl = userMapper.convertToOnlineDTO(user);
            onl.setSessionId(sessionId);
            this.onlineUsers.add(onl);
        }
    }

    @EventListener
    public void handleWebSocketListener(SessionDisconnectEvent event){
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompAccessor.getSessionId();

        UserOnlineDTO user = this.onlineUsers
                .stream()
                .filter(e -> e.getSessionId().equals(sessionId))
                .toList().get(0);
        this.onlineUsers.remove(user);
    }
}
