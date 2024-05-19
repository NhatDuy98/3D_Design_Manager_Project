package org.design_manager_project.repositories;

import org.design_manager_project.models.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
