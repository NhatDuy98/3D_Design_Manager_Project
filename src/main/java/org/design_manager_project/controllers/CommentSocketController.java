package org.design_manager_project.controllers;

import lombok.AllArgsConstructor;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.services.CommentService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class CommentSocketController {
    private final CommentService commentService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/cards/{id}/comments")
    @SendTo("/topic/comments")
    public ApiResponse createCardComment(
            @Payload CommentDTO dto,
            @DestinationVariable("id") String cardId
    ){
        CommentDTO commentDTO = commentService.createCardComment(UUID.fromString(cardId), dto);
        return ApiResponse.success(commentDTO);
    }

    @MessageMapping("/prints/{id}/comments")
    @SendTo("/topic/comments")
    public ApiResponse createPrintComment(
            @Payload CommentDTO dto,
            @DestinationVariable("id") String printId
    ){
        CommentDTO commentDTO = commentService.createPrintComment(UUID.fromString(printId), dto);
        return ApiResponse.success(commentDTO);
    }

    @MessageMapping("/comments/{id}/update")
    @SendTo("/topic/comments")
    public ApiResponse updateComment(
            @Payload CommentDTO dto,
            @DestinationVariable("id") String id
    ){
        CommentDTO commentDTO = commentService.updateComment(UUID.fromString(id), dto);
        return ApiResponse.success(commentDTO);
    }

    @MessageMapping("/comments/{id}/delete")
    public void deleteComment(
            @DestinationVariable("id") String id
    ){
        commentService.deleteComment(UUID.fromString(id));
    }

}
