package org.design_manager_project.repository;

import org.design_manager_project.model.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepositoty extends JpaRepository<Space, Integer> {
}
