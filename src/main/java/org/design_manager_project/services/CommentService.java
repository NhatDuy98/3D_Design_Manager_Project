package org.design_manager_project.services;

import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.filters.CommentFilter;
import org.design_manager_project.mappers.CommentMapper;
import org.design_manager_project.models.entity.Comment;
import org.design_manager_project.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService extends BaseService<Comment, CommentDTO, CommentFilter, UUID> {

    private final CommentRepository commentRepository;
    private static final CommentMapper commentMapper = CommentMapper.INSTANCE;

    protected CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        super(commentRepository, commentMapper);
        this.commentRepository = commentRepository;
    }
}
