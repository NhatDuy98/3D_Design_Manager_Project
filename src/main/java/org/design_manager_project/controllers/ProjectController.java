package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.project.ProjectDTO;
import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.filters.ProjectFilter;
import org.design_manager_project.models.entity.Project;
import org.design_manager_project.services.CardService;
import org.design_manager_project.services.MemberService;
import org.design_manager_project.services.PrintService;
import org.design_manager_project.services.ProjectService;
import org.springframework.http.ResponseEntity;
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
    private final PrintService printService;

    protected ProjectController(ProjectService projectService, MemberService memberService, CardService cardService, PrintService printService) {
        super(projectService);
        this.projectService = projectService;
        this.memberService = memberService;
        this.cardService = cardService;
        this.printService = printService;
    }

    @GetMapping("/{id}/members")
    public ApiResponse getAllMembersOnlineWithProject(
            @PathVariable("id") UUID projectId
    ){
        return ApiResponse.success(memberService.getAllMembersOnlineWithProject(projectId));
    }

    @GetMapping("/{id}/cards")
    public ApiResponse getAllCardsWithProject(
            @PathVariable("id") UUID projectId,
            CardFilter filter
    ){
        return ApiResponse.success(cardService.getAllCardsWithProject(projectId, filter));
    }

    @GetMapping("/{id}/prints")
    public ResponseEntity<ApiResponse> getAllPrintsWithProject(
            @PathVariable("id") UUID projectId,
            PrintFilter filter
    ){
        return ResponseEntity.ok(ApiResponse.success(printService.getAllPrintsWithProject(projectId, filter)));
    }
}
