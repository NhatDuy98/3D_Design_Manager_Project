package org.design_manager_project.mappers;

import org.design_manager_project.dtos.user.request.UserRequest;
import org.design_manager_project.dtos.user.response.UserResponse;
import org.design_manager_project.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapperImpl<User, UserRequest, UserResponse, UUID>{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    @Mapping(target = "isActive", constant = "true")
    User convertToEntityForCreate(UserRequest userRequest);

    @Override
    @Mapping(source = "active", target = "active", ignore = true)
    User updateEntity(User updated,@MappingTarget User entity);

    @Override
    default List<User> convertListToEntityForCreate(List<UserRequest> userRequests){
        return userRequests.stream().map(e -> convertToEntityForCreate(e)).toList();
    }

    @Override
    default Page<UserResponse> convertPageToDTO(Page<User> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<UserResponse> convertOptional(Optional<User> user){
        return user.map(e -> convertToDTO(e));
    }
}
