package org.design_manager_project.repositories;

import org.design_manager_project.filters.ProjectFilter;
import org.design_manager_project.models.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD

import java.util.UUID;

@Repository
public interface ProjectRepository extends BaseRepository<Project, ProjectFilter, UUID> {

    @Override
    @Query("""
        SELECT p FROM Project p 
        WHERE (:#{#filter.search == null || #filter.search.isEmpty()} = TRUE 
            OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%')))
        AND :#{#filter.id == null} = TRUE OR p.space.id = :#{#filter.id}
""")
    Page<Project> findAllWithFilter(Pageable pageable, ProjectFilter filter);


    @Query("""
        SELECT p FROM Project p 
        LEFT JOIN Member m ON p.id = m.project.id 
        WHERE m.user.id = :#{#filter.userId}

""")
    Page<Project> findAllProjectsWithUser(Pageable pageable, ProjectFilter filter);
=======

import java.util.UUID;

@Repository
public interface ProjectRepository extends BaseRepository<Project, ProjectFilter, UUID> {

    @Override
    @Query("""
        SELECT p FROM Project p 
        WHERE (:#{#filter.search == null || #filter.search.isEmpty()} = TRUE 
            OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%'))
        )
""")
    Page<Project> findAllWithFilter(Pageable pageable, ProjectFilter filter);


>>>>>>> dea9dbf8a6b7c5571c7fb46fcc99091044abf573
}
