package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.member.MemberDTO;
import org.design_manager_project.filters.MemberFilter;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.services.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberController extends BaseController<Member, MemberDTO, MemberFilter, UUID> {

    private final MemberService memberService;

    protected MemberController(MemberService memberService) {
        super(memberService);
        this.memberService = memberService;
    }

    @GetMapping("/projects/{id}")
    public ApiResponse getAllMembersWithProject(
            @PathVariable("id") UUID projectId,
            MemberFilter filter
    ){
        Page<MemberDTO> memberDTOS = memberService.getAllMembersWithProject(projectId, filter);

        if (memberDTOS.isEmpty()){
            List<ApiResponse> apiResponses = new ArrayList<>();

            return ApiResponse.success(apiResponses);
        }

        return ApiResponse.success(memberDTOS);
    }
}
