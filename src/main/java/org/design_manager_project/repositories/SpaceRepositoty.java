package org.design_manager_project.repositories;

import org.design_manager_project.models.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepositoty extends JpaRepository<Space, Integer> {
}
