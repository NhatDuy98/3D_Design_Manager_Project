package org.design_manager_project.controllers;

import org.design_manager_project.dtos.card_member.CardMemberDTO;
import org.design_manager_project.filters.CardMemberFilter;
import org.design_manager_project.models.entity.CardMember;
import org.design_manager_project.services.CardMemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/card-members")
public class CardMemberController extends BaseController<CardMember, CardMemberDTO, CardMemberFilter, UUID> {

    protected CardMemberController(CardMemberService cardMemberService) {
        super(cardMemberService);
    }
}
