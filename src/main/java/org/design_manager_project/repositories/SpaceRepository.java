package org.design_manager_project.repositories;

import org.design_manager_project.filter.SpaceFilter;
import org.design_manager_project.models.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpaceRepository extends BaseRepository<Space, SpaceFilter, UUID> {

    @Override
    @Query("""
            SELECT s FROM Space s 
            WHERE (:#{#filter.search == null || #filter.search.isEmpty()} = TRUE 
                OR LOWER(s.spaceName) LIKE LOWER(CONCAT('%', :#{#filter.search},'%'))) 
            AND (:#{#filter.userId == null} = TRUE OR s.user.id = :#{#filter.userId})

""")
    Page<Space> findAllWithFilter(Pageable pageable, SpaceFilter filter);
}
