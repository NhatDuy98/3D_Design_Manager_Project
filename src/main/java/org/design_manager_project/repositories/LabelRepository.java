package org.design_manager_project.repositories;

import org.design_manager_project.models.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Integer> {
}
