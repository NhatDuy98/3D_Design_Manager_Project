package org.design_manager_project.mappers;

import org.design_manager_project.dtos.notification.NotificationDTO;
import org.design_manager_project.models.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper extends BaseMapper<Notification, NotificationDTO> {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Override
    default Page<NotificationDTO> convertPageToDTO(Page<Notification> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<NotificationDTO> convertOptional(Optional<Notification> notification){
        return notification.map(e -> convertToDTO(e));
    }
}
