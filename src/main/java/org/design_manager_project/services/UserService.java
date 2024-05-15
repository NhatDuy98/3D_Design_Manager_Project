package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.user.request.UserRequest;
import org.design_manager_project.dtos.user.response.UserResponse;
import org.design_manager_project.exeptions.DeletedException;
import org.design_manager_project.exeptions.EmailExistsException;
import org.design_manager_project.exeptions.UserNotActiveException;
import org.design_manager_project.filter.UserFilter;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService<User, UserRequest, UserResponse, UserFilter, UUID>{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    protected UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private void checkEmailCreate(UserRequest userRequest){
        User e = userRepository.findUserByEmail(userRequest.getEmail());

        if (e != null){
            throw new EmailExistsException("Email already exists");
        }
    }

    private void checkEmailUpdate(UUID id, UserRequest userRequest){

        User eRepo = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        if (!eRepo.isActive()){
            throw new UserNotActiveException("User not active");
        }

        if (eRepo.getDeletedAt() != null){
            throw new DeletedException("User deleted");
        }

        User e = userRepository.findUserByEmail(userRequest.getEmail());

        if (e != null && !eRepo.getEmail().equals(userRequest.getEmail())){
            throw new EmailExistsException("Email already exists");
        }

    }

    private void checkDeleted(User user){
        if (user.getDeletedAt() != null){
            throw new DeletedException("User with id: " + user.getId() + " already deleted");
        }
    }

    @Override
    public UserResponse create(UserRequest request) {
        checkEmailCreate(request);

        return super.create(request);
    }

    @Override
    public UserResponse update(UUID uuid, UserRequest request) {
        checkEmailUpdate(uuid, request);

        return super.update(uuid, request);
    }

    @Override
    public List<UserResponse> updateAll(List<UserRequest> userRequests) {
        userRequests.forEach(e -> checkEmailUpdate(e.getId(), e));
        return super.updateAll(userRequests);
    }

    @Override
    public void deleteById(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + uuid));

        checkDeleted(user);

        user.setDeletedAt(Instant.now());

        userRepository.save(user);
    }

    @Override
    public void deleteAll(List<UUID> uuids) {
        List<User> users = userRepository.findAllById(uuids).stream().map(e -> {
            checkDeleted(e);

            e.setDeletedAt(Instant.now());

            return e;
        }).toList();

        userRepository.saveAll(users);
    }
}
