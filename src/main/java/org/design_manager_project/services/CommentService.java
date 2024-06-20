package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.filters.CommentFilter;
import org.design_manager_project.mappers.CommentMapper;
import org.design_manager_project.models.entity.Comment;
import org.design_manager_project.repositories.CardRepository;
import org.design_manager_project.repositories.CommentRepository;
import org.design_manager_project.repositories.PrintRepository;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.BiFunction;

import static org.design_manager_project.utils.Constants.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService{

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final PrintRepository printRepository;
    private static final CommentMapper commentMapper = CommentMapper.INSTANCE;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public Page<CommentDTO> getAllComments(CommentFilter commentFilter) {
        return commentMapper.convertPageToDTO(commentRepository.findAllWithFilter(commentFilter.getPageable(), commentFilter));
    }

    private Comment findAndAssignCard(UUID cardId, CommentDTO dto) {
        var card = cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));
        var comment = commentMapper.convertToEntity(dto);
        comment.setCard(card);
        return comment;
    }

    private Comment findAndAssignPrint(UUID printId, CommentDTO dto) {
        var print = printRepository.findById(printId).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));
        var comment = commentMapper.convertToEntity(dto);
        comment.setPrint(print);
        return comment;
    }

    public CommentDTO createComment(UUID prefixId, CommentDTO dto, BiFunction<UUID, CommentDTO, Comment> findAndAssignEntity) {
        Comment comment = findAndAssignEntity.apply(prefixId, dto);
        commentRepository.save(comment);
        return commentMapper.convertToDTO(comment);
    }

    public void createCardComment(UUID prefixId, CommentDTO dto) {
        simpMessageSendingOperations.convertAndSend(
                "/topic/cards/" + prefixId + "/comments",
                ApiResponse.success(createComment(prefixId, dto, this::findAndAssignCard))
        );
    }

    public void createPrintComment(UUID prefixId, CommentDTO dto) {
        simpMessageSendingOperations.convertAndSend(
                "/topic/prints/" + prefixId + "/comments",
                ApiResponse.success(createComment(prefixId, dto, this::findAndAssignPrint))
        );
    }

    public void updateComment(UUID id, CommentDTO dto) {
        var comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        comment.setContent(dto.getContent());

        commentRepository.save(comment);

        simpMessageSendingOperations.convertAndSend(
                "/topic/comments/" + id,
                ApiResponse.success(commentMapper.convertToDTO(comment))
        );
    }

    public void deleteComment(UUID uuid) {
        commentRepository.deleteById(uuid);
    }

    public Page<CommentDTO> getAllCommentsWithCard(UUID cardId) {
        CommentFilter filter = new CommentFilter();

        filter.setCardId(cardId);

        return commentMapper.convertPageToDTO(commentRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    public Page<CommentDTO> getAllCommentsWithPrint(UUID printId) {
        CommentFilter filter = new CommentFilter();

        filter.setPrintId(printId);

        return commentMapper.convertPageToDTO(commentRepository.findAllWithFilter(filter.getPageable(), filter));
    }
}
