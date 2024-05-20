package org.design_manager_project.repositories;

import org.design_manager_project.filter.SpaceFilter;
import org.design_manager_project.models.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpaceRepository extends BaseRepository<Space, SpaceFilter, UUID> {

    @Override
    @Query("""
            SELECT s FROM Space s 
            WHERE LOWER(s.spaceName) LIKE LOWER(CONCAT('%', :#{#spaceFilter.search},'%') ) 

""")
    Page<Space> findAllWithFilter(Pageable pageable, SpaceFilter spaceFilter);


    @Query("""
            SELECT s FROM Space s 
            LEFT JOIN User u ON s.user.id = u.id 
            WHERE u.id = :userId

""")
    Optional<Space> findWithUserId(UUID userId);

}
