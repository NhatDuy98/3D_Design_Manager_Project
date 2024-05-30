package org.design_manager_project.repositories;

import org.design_manager_project.filters.UserFilter;
import org.design_manager_project.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UserFilter, UUID> {
    @Override
    @Query("""
            SELECT u FROM User u
            WHERE (:#{#filter.search == null || #filter.search.isEmpty()} = TRUE 
                OR (LOWER(u.firstName) LIKE LOWER(CONCAT('%',:#{#filter.search},'%')) 
                    OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:#{#filter.search},'%')) 
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%',:#{#filter.search},'%'))))
            AND (:#{#filter.active == null} = TRUE OR u.isActive = :#{#filter.active})
    """)
    Page<User> findAllWithFilter(Pageable pageable, UserFilter filter);

    User findUserByEmail(String email);

}
