package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.design_manager_project.dtos.notification.NotificationDTO;
import org.design_manager_project.filters.NotificationFilter;
import org.design_manager_project.mappers.NotificationMapper;
import org.design_manager_project.repositories.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.design_manager_project.utils.Constants.DATA_NOT_FOUND;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private static final NotificationMapper mapper = NotificationMapper.INSTANCE;
    public Page<NotificationDTO> getAllNotificationsOfMember(UUID memberId) {
        var filter = new NotificationFilter();
        filter.setMemberId(memberId);
        return mapper.convertPageToDTO(notificationRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    public NotificationDTO updateStatusNotification(UUID id) {
        var notification = notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        notification.setIsRead(Boolean.TRUE);

        notificationRepository.save(notification);

        return mapper.convertToDTO(notification);
    }

    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }
}
