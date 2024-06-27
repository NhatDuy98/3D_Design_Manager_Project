package org.design_manager_project.controllers;

import lombok.AllArgsConstructor;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStatusNotification(
            @PathVariable("id")UUID id
    ){
        var dto = notificationService.updateStatusNotification(id);

        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNotification(
            @PathVariable("id") UUID id
    ){
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
