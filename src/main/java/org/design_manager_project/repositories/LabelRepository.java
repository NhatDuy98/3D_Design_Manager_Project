package org.design_manager_project.repositories;

import org.design_manager_project.filters.LabelFilter;
import org.design_manager_project.models.entity.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LabelRepository extends BaseRepository<Label, LabelFilter, UUID> {

    @Override
    @Query("""
        SELECT l FROM Label l 
        WHERE (:#{#filter.search == NULL || #filter.search.isEmpty()} = TRUE 
        OR LOWER(l.labelName) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%'))
        OR LOWER(l.color) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%'))) 
        AND (:#{#filter.projectId == NULL} = TRUE OR l.project.id = :#{#filter.projectId})

""")
    Page<Label> findAllWithFilter(Pageable pageable, LabelFilter filter);
}
