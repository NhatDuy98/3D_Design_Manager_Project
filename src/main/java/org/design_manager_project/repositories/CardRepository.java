package org.design_manager_project.repositories;

import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.models.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRepository extends BaseRepository<Card, CardFilter, UUID> {

    @Override
    @Query("""
        SELECT c FROM Card c 
        WHERE (:#{#filter.search == null || #filter.search.isEmpty()} = TRUE 
            OR LOWER(c.cardName) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%')))
        AND (:#{#filter.status == null} = TRUE OR c.status = :#{#filter.status})
        AND (:#{#filter.projectId == null} = TRUE OR c.project.id = :#{#filter.projectId})
        AND (c.deletedAt = null )

""")
    Page<Card> findAllWithFilter(Pageable pageable, CardFilter filter);
}
