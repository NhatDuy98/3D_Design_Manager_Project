package org.design_manager_project.repositories;

import org.design_manager_project.models.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
