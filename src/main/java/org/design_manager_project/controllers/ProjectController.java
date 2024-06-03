package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.project.ProjectDTO;
import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.filters.MemberFilter;
import org.design_manager_project.filters.ProjectFilter;
import org.design_manager_project.models.entity.Project;
import org.design_manager_project.services.CardService;
import org.design_manager_project.services.MemberService;
import org.design_manager_project.services.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController extends BaseController<Project, ProjectDTO, ProjectFilter, UUID> {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final CardService cardService;

    protected ProjectController(ProjectService projectService, MemberService memberService, CardService cardService) {
        super(projectService);
        this.projectService = projectService;
        this.memberService = memberService;
        this.cardService = cardService;
    }

    @GetMapping("/{id}/members")
    public ApiResponse getAllMembersWithProject(
            @PathVariable("id") UUID projectId,
            MemberFilter filter
    ){
        return ApiResponse.success(memberService.getAllMembersWithProject(projectId, filter));
    }

    @GetMapping("/{id}/cards")
    public ApiResponse getAllCardsWithProject(
            @PathVariable("id") UUID projectId,
            CardFilter filter
    ){
        return ApiResponse.success(cardService.getAllCardsWithProject(projectId, filter));
    }
}
