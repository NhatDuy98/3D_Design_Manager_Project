package org.design_manager_project.dtos.card_member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.card.request.CardRequestWithID;
import org.design_manager_project.dtos.member.request.MemberRequestWithID;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardMemberDTO extends BaseDTO<UUID> {
    private CardRequestWithID card;
    private MemberRequestWithID member;
}
