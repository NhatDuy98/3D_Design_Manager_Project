package org.design_manager_project.repository;

import org.design_manager_project.model.Print;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintRepository extends JpaRepository<Print, Integer> {
}
