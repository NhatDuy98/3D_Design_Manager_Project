package org.design_manager_project.mappers;

import org.design_manager_project.dtos.comment.CommentDTO;
import org.design_manager_project.models.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper extends BaseMapper<Comment, CommentDTO>{
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Override
    default Optional<CommentDTO> convertOptional(Optional<Comment> comment){
        return comment.map(e -> convertToDTO(e));
    }

    @Override
    default Page<CommentDTO> convertPageToDTO(Page<Comment> pageE){
        return pageE.map(e -> convertToDTO(e));
    }
}
