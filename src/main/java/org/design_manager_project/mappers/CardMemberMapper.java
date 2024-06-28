package org.design_manager_project.mappers;

import org.design_manager_project.dtos.card_member.CardMemberDTO;
import org.design_manager_project.models.entity.CardMember;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMemberMapper extends BaseMapper<CardMember, CardMemberDTO> {
    CardMemberMapper INSTANCE = Mappers.getMapper(CardMemberMapper.class);

    @Override
    default Page<CardMemberDTO> convertPageToDTO(Page<CardMember> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<CardMemberDTO> convertOptional(Optional<CardMember> cardMember){
        return cardMember.map(e -> convertToDTO(e));
    }
}
