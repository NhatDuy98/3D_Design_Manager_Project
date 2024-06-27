package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.member.MemberDTO;
import org.design_manager_project.dtos.notification.NotificationDTO;
import org.design_manager_project.filters.MemberFilter;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.services.MemberService;
import org.design_manager_project.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberController extends BaseController<Member, MemberDTO, MemberFilter, UUID> {

    private final MemberService memberService;
    private final NotificationService notificationService;

    protected MemberController(MemberService memberService, NotificationService notificationService) {
        super(memberService);
        this.memberService = memberService;
        this.notificationService = notificationService;
    }
    
    @GetMapping("/{id}/notifications")
    public ResponseEntity<ApiResponse> getAllNotificationsOfMember(
            @PathVariable("id") UUID memberId
    ){
        Page<NotificationDTO> notifications = notificationService.getAllNotificationsOfMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }
    
}
