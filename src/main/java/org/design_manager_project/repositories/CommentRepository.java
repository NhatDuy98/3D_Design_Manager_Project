package org.design_manager_project.repositories;

import org.design_manager_project.filters.CommentFilter;
import org.design_manager_project.models.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends BaseRepository<Comment, CommentFilter, UUID> {

    @Override
    @Query("""
        SELECT c FROM Comment c 
        WHERE (:#{#filter.cardId == NULL} = TRUE OR c.card.id = :#{#filter.cardId}) 
         OR (:#{#filter.printId == NULL} = TRUE OR c.print.id = :#{#filter.printId})

""")
    Page<Comment> findAllWithFilter(Pageable pageable, CommentFilter filter);
}
