package org.design_manager_project.repositories;

import org.design_manager_project.filters.NotificationFilter;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.models.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends BaseRepository<Notification, NotificationFilter, UUID> {

    @Override
    @Query("""
        SELECT n FROM Notification n 
        WHERE (:#{#filter.memberId == NULL} = TRUE OR n.member.id = :#{#filter.memberId}) 
         OR (:#{#filter.cardId == NULL} = TRUE OR n.card.id = :#{#filter.cardId}) 
         OR (:#{#filter.printId == NULL} = TRUE OR n.print.id = :#{#filter.printId}) 
         OR (:#{#filter.commentId == NULL} = TRUE OR n.comment.id = :#{#filter.commentId})
""")
    Page<Notification> findAllWithFilter(Pageable pageable, NotificationFilter filter);

    boolean existsByCard(Card card);
}
