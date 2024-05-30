package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.user.UserDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.UserFilter;
import org.design_manager_project.mappers.UserMapper;
import org.design_manager_project.models.entity.User;
import org.design_manager_project.repositories.UserRepository;
import org.design_manager_project.utils.Constants;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService<User, UserDTO, UserFilter, UUID>{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    protected UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void validateEmailCreate(String email){
        User e = userRepository.findUserByEmail(email);

        if (e != null){
            throw new BadRequestException(Constants.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateEmailUpdate(UUID id, UserDTO userRequest){

        User eRepo = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Constants.DATA_NOT_FOUND));

        if (Boolean.FALSE.equals(eRepo.getIsActive())){
            throw new BadRequestException(Constants.NOT_ACTIVE);
        }

        if (eRepo.getDeletedAt() != null){
            throw new BadRequestException(Constants.OBJECT_DELETED);
        }

        User e = userRepository.findUserByEmail(userRequest.getEmail());

        if (e != null && !eRepo.getEmail().equals(userRequest.getEmail())){
            throw new BadRequestException(Constants.EMAIL_ALREADY_EXISTS);
        }

    }

    private void checkDeleted(User user){
        if (user.getDeletedAt() != null){
            throw new BadRequestException(Constants.OBJECT_DELETED);
        }
    }

    @Override
    public UserDTO create(UserDTO request) {
        validateEmailCreate(request.getEmail());

        return super.create(request);
    }

    @Override
    public List<UserDTO> createAll(List<UserDTO> list) {
        list.forEach(e -> validateEmailCreate(e.getEmail()));
        return super.createAll(list);
    }

    @Override
    public UserDTO update(UUID uuid, UserDTO request) {
        validateEmailUpdate(uuid, request);

        return super.update(uuid, request);
    }

    @Override
    public List<UserDTO> updateAll(List<UserDTO> userRequests) {
        userRequests.forEach(e -> validateEmailUpdate(e.getId(), e));
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
