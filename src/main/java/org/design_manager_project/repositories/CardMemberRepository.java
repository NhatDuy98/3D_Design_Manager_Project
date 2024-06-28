package org.design_manager_project.repositories;

import org.design_manager_project.filters.CardMemberFilter;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.models.entity.CardMember;
import org.design_manager_project.models.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardMemberRepository extends BaseRepository<CardMember, CardMemberFilter, UUID>{

    @Override
    @Query("""
        SELECT cm FROM CardMember cm 
        WHERE (:#{#filter.cardId == NULL} = TRUE OR cm.card.id = :#{#filter.cardId}) 
         OR (:#{#filter.memberId == NULL} = TRUE OR cm.member.id = :#{#filter.memberId})
""")
    Page<CardMember> findAllWithFilter(Pageable pageable, CardMemberFilter filter);

    boolean existsByCardAndMember(Card card, Member member);
}
