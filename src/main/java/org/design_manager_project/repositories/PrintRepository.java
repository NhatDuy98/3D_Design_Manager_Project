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
        LEFT JOIN Version v ON p.id = v.print.id 
        WHERE (:#{#filter.cardId == null} = TRUE OR :#{#filter.cardId} = p.card.id) 
        AND (:#{#filter.latestId == null} = TRUE OR :#{#filter.latestId} = v.id)

""")
    Page<Print> findAllWithFilter(Pageable pageable, PrintFilter filter);
}
