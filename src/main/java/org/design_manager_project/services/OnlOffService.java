package org.design_manager_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.design_manager_project.dtos.user.response.UserStatusDTO;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.models.enums.StatusUser;
import org.design_manager_project.repositories.UserRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@AllArgsConstructor
public class OnlOffService {
    private final Map<UUID, Set<UUID>> projectOnlUsers = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> projectSubUsers = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public void addOnlineUser(Principal user, UUID projectId){
        if (user == null) return;

        User userDetails = getUserDetails(user);
        log.info("{} is online", userDetails.getUsername());
        simpMessageSendingOperations.convertAndSend(
                "/topic/status/" + projectId,
                UserStatusDTO.builder()
                        .id(userDetails.getId())
                        .firstName(userDetails.getFirstName())
                        .lastName(userDetails.getLastName())
                        .email(userDetails.getEmail())
                        .status(String.valueOf(StatusUser.ONLINE))
                        .build()
        );
        this.projectOnlUsers.put(projectId, Collections.singleton(userDetails.getId()));
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
            Set<UUID> users = this.projectOnlUsers.get(projectId);
            if (users != null){
                users.remove(userDetails.getId());
                if (users.isEmpty()){
                    this.projectOnlUsers.remove(projectId);
                }
            }
            simpMessageSendingOperations.convertAndSend(
                    "/topic/status/" + projectId,
                    UserStatusDTO.builder()
                            .id(userDetails.getId())
                            .firstName(userDetails.getFirstName())
                            .lastName(userDetails.getLastName())
                            .email(userDetails.getEmail())
                            .status(String.valueOf(StatusUser.OFFLINE))
                            .build()
            );
        }
    }

    public List<UserStatusDTO> getOnlineUsersWithProject(UUID projectId){
        Set<UUID> userIds = this.projectOnlUsers.getOrDefault(projectId, new HashSet<>());
        List<User> users = userRepository.findAllById(userIds);
        return userMapper.convertToOnlineDTOs(users).stream().map(e -> {
            e.setStatus(String.valueOf(StatusUser.ONLINE));
            return e;
        }).toList();
    }

    public void addUserSubscribed(Principal user, String subscribedChannel, UUID projectId){
        var userDetails = getUserDetails(user);
        log.info("{} subscribed to {}", userDetails.getUsername(), subscribedChannel);

        this.projectSubUsers.put(projectId, Collections.singleton(userDetails.getId()));

        simpMessageSendingOperations.convertAndSend(
                subscribedChannel,
                UserStatusDTO.builder()
                        .id(userDetails.getId())
                        .firstName(userDetails.getFirstName())
                        .lastName(userDetails.getLastName())
                        .email(userDetails.getEmail())
                        .status(String.valueOf(StatusUser.TRACKING))
                        .build()
        );
    }
    public void removeUserSubscribed(Principal user, String subscribedChannel, UUID projectId){
        var userDetails = getUserDetails(user);
        log.info("unsubscription! {} unsubscribed {}", userDetails.getUsername(), subscribedChannel);

        simpMessageSendingOperations.convertAndSend(
                subscribedChannel,
                UserStatusDTO.builder()
                        .id(userDetails.getId())
                        .firstName(userDetails.getFirstName())
                        .lastName(userDetails.getLastName())
                        .email(userDetails.getEmail())
                        .status(String.valueOf(StatusUser.UNTRACKING))
                        .build()
        );

        Set<UUID> users = this.projectSubUsers.get(projectId);
        if (users != null){
            users.remove(userDetails.getId());
            if (users.isEmpty()){
                this.projectOnlUsers.remove(projectId);
            }
        }
    }

    public List<UserStatusDTO> getSubUsersWithProject(UUID projectId){
        Set<UUID> userIds = this.projectSubUsers.getOrDefault(projectId, new HashSet<>());
        List<User> users = userRepository.findAllById(userIds);
        return userMapper.convertToOnlineDTOs(users).stream().map(e -> {
            e.setStatus(String.valueOf(StatusUser.TRACKING));
            return e;
        }).toList();
    }


}
