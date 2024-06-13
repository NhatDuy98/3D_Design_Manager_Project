package org.design_manager_project.mappers;

import org.design_manager_project.dtos.AuthenticationResponse;
import org.design_manager_project.dtos.user.UserDTO;
import org.design_manager_project.dtos.user.request.UserRequestForRegister;
import org.design_manager_project.dtos.user.response.UserOnlineDTO;
import org.design_manager_project.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDTO>{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    UserOnlineDTO convertToOnlineDTO(User user);

    @Override
    @Mapping(target = "password", constant = "123456")
    @Mapping(target = "isActive", constant = "true")
    User convertToEntity(UserDTO dto);

    @Override
    @Mapping(source = "password", target = "password", ignore = true)
    UserDTO convertToDTO(User entity);

    @Mapping(target = "isActive", constant = "true")
    User convertForRegister(UserRequestForRegister userRequestForRegister);


    AuthenticationResponse convertForAuth(User user);

    @Override
    default Page<UserDTO> convertPageToDTO(Page<User> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<UserDTO> convertOptional(Optional<User> user){
        return user.map(e -> convertToDTO(e));
    }
}
