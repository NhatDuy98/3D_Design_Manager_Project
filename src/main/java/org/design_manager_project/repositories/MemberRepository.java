package org.design_manager_project.repositories;

import org.design_manager_project.filter.MemberFilter;
import org.design_manager_project.models.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends BaseRepository<Member, MemberFilter, UUID> {

    @Override
    @Query("""
            SELECT m FROM Member m 
            WHERE LOWER(m.role) LIKE LOWER(CONCAT('%', :#{#memberFilter.search}, '%')) 

""")
    Page<Member> findAllWithFilter(Pageable pageable, MemberFilter memberFilter);

    @Query("""
            SELECT m FROM Member m 
            WHERE m.space.id = :spaceId AND m.user.id = :userId

""")
    Optional<Member> findMemberWithSpaceAndUser(UUID spaceId, UUID userId);
}
