package org.design_manager_project.controllers;

import lombok.AllArgsConstructor;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static org.design_manager_project.utils.Constants.BAD_REQUEST;

@Controller
@AllArgsConstructor
public class CommentSocketController {
    private final CommentService commentService;

    @MessageMapping("/comments")
    @SendTo("/app/comments")
    public ResponseEntity<ApiResponse> createComment(
            CommentDTO dto
    ){
        CommentDTO commentDTO = commentService.create(dto);
        if (commentDTO != null){
            return ResponseEntity.ok(ApiResponse.success(commentDTO));
        }else {
            throw new BadRequestException(BAD_REQUEST);
        }
    }

}
