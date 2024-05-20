package org.design_manager_project.repositories;

import org.design_manager_project.filter.UserFilter;
import org.design_manager_project.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UserFilter, UUID> {

    @Query("SELECT u FROM User u WHERE (LOWER(u.firstName) LIKE LOWER(:search) OR " +
            "LOWER(u.lastName) LIKE LOWER(:search) OR " +
            "LOWER(u.email) LIKE LOWER(:search)) " +
            "AND u.isActive = :isActive ")
    Page<User> findAllWithSearchAndFilter(Pageable pageable, String search, boolean isActive);

    @Override
    @Query("""
            SELECT u FROM User u
            WHERE (LOWER(u.firstName) LIKE LOWER(CONCAT('%',:#{#filter.search},'%')) 
            OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:#{#filter.search},'%')) 
            OR LOWER(u.email) LIKE LOWER(CONCAT('%',:#{#filter.search},'%')) )
            AND u.isActive = :#{#filter.active}
    """)
    Page<User> findAllWithFilter(Pageable pageable, UserFilter filter);


    User findUserByEmail(String email);

}