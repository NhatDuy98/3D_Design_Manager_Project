package org.design_manager_project.controllers;


import lombok.AllArgsConstructor;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.filters.CommentFilter;
import org.design_manager_project.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @GetMapping
    public ResponseEntity<ApiResponse> getAllComments(
            CommentFilter commentFilter
    ){
        Page<CommentDTO> dtos = commentService.getAllComments(commentFilter);
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

}
