package org.design_manager_project.repositories;

import org.design_manager_project.filter.ProjectFilter;
import org.design_manager_project.models.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
