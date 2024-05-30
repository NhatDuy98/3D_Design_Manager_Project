package org.design_manager_project.controllers;

import org.design_manager_project.dtos.member.MemberDTO;
import org.design_manager_project.filters.MemberFilter;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.services.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberController extends BaseController<Member, MemberDTO, MemberFilter, UUID> {

    private final MemberService memberService;

    protected MemberController(MemberService memberService) {
        super(memberService);
        this.memberService = memberService;
    }
}
