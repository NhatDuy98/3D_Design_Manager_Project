package org.design_manager_project.repositories;

import org.design_manager_project.models.entity.Print;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintRepository extends JpaRepository<Print, Integer> {
}
