package org.design_manager_project.mappers;

import org.design_manager_project.dtos.card.CardDTO;
import org.design_manager_project.models.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper extends BaseMapper<Card, CardDTO>{
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Override
    default Page<CardDTO> convertPageToDTO(Page<Card> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<CardDTO> convertOptional(Optional<Card> card){
        return card.map(e -> convertToDTO(e));
    }
}
