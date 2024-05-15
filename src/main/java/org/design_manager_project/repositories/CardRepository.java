package org.design_manager_project.repositories;

import org.design_manager_project.models.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
