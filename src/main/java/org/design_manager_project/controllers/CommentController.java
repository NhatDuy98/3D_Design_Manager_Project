package org.design_manager_project.controllers;


import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.filters.CommentFilter;
import org.design_manager_project.models.entity.Comment;
import org.design_manager_project.services.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController extends BaseController<Comment, CommentDTO, CommentFilter, UUID> {
    private final CommentService commentService;
    protected CommentController(CommentService commentService) {
        super(commentService);
        this.commentService = commentService;
    }



}
