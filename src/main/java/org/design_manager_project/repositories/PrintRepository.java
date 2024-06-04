package org.design_manager_project.repositories;

import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.models.entity.Print;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrintRepository extends BaseRepository<Print, PrintFilter, UUID> {

    @Override
    @Query("""
        SELECT p FROM Print p 
        LEFT JOIN Card c ON c.id = p.card.id  
        WHERE (:#{#filter.cardId == NULL} = TRUE OR :#{#filter.cardId} = p.card.id) 
        AND (:#{#filter.projectId == NULL} = TRUE OR :#{#filter.projectId} = c.project.id) 

""")
    Page<Print> findAllWithFilter(Pageable pageable, PrintFilter filter);

}
