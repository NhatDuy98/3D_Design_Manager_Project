package org.design_manager_project.controllers;

import lombok.AllArgsConstructor;
import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.services.CommentService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class CommentSocketController {
    private final CommentService commentService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/cards/{id}/comments")
    public void createCardComment(
            @Payload CommentDTO dto,
            @DestinationVariable("id") String cardId
    ){
        commentService.createCardComment(UUID.fromString(cardId), dto);
    }

    @MessageMapping("/prints/{id}/comments")
    public void createPrintComment(
            @Payload CommentDTO dto,
            @DestinationVariable("id") String printId
    ){
        commentService.createPrintComment(UUID.fromString(printId), dto);
    }

    @MessageMapping("/comments/{id}/update")
    public void updateComment(
            @Payload CommentDTO dto,
            @DestinationVariable("id") String id
    ){
        commentService.updateComment(UUID.fromString(id), dto);
    }

    @MessageMapping("/comments/{id}/delete")
    public void deleteComment(
            @DestinationVariable("id") String id
    ){
        commentService.deleteComment(UUID.fromString(id));
    }

}
