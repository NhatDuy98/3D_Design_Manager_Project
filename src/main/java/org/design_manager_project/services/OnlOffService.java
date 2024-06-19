package org.design_manager_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.design_manager_project.dtos.user.response.UserOnlineDTO;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.models.enums.StatusUser;
import org.design_manager_project.repositories.MemberRepository;
import org.design_manager_project.repositories.UserRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
@Slf4j
@AllArgsConstructor
public class OnlOffService {
    private final Set<UUID> onlineUsers = new ConcurrentSkipListSet<>();
    private final Map<UUID, Set<String>> userSubcribed = new ConcurrentHashMap<>();
    private MemberRepository memberRepository;
    private UserRepository userRepository;
    private UserMapper userMapper = UserMapper.INSTANCE;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public void addOnlineUser(Principal user, UUID projectId){
        if (user == null) return;

        User userDetails = getUserDetails(user);
        log.info("{} is online", userDetails.getUsername());
        simpMessageSendingOperations.convertAndSend(
                "/topic/status" + projectId,
                UserOnlineDTO.builder()
                        .id(userDetails.getId())
                        .firstName(userDetails.getFirstName())
                        .lastName(userDetails.getLastName())
                        .email(userDetails.getEmail())
                        .status(String.valueOf(StatusUser.ONLINE))
                        .build()
        );
        onlineUsers.add(userDetails.getId());
    }
    private User getUserDetails(Principal principal){
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        Object o = user.getPrincipal();
        return (User) o;
    }

    public void removeOnlineUser(Principal user, UUID projectId){
        if (user != null){
            User userDetails = getUserDetails(user);
            log.info("{} went offline", userDetails.getUsername());
            onlineUsers.remove(userDetails.getId());
            simpMessageSendingOperations.convertAndSend(
                    "/topic/status" + projectId,
                    UserOnlineDTO.builder()
                            .id(userDetails.getId())
                            .firstName(userDetails.getFirstName())
                            .lastName(userDetails.getLastName())
                            .email(userDetails.getEmail())
                            .status(String.valueOf(StatusUser.OFFLINE))
                            .build()
            );
        }
    }
    public boolean isUserOnline(UUID userId){
        return onlineUsers.contains(userId);
    }

    public List<UserOnlineDTO> getOnlineUsersWithProject(List<User> users){
        return userMapper.convertToOnlineDTOs(users);
    }

}
