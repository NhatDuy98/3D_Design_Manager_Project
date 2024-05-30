package org.design_manager_project.mappers;

import org.design_manager_project.dtos.member.MemberDTO;
import org.design_manager_project.models.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MemberMapper extends BaseMapper<Member, MemberDTO>{
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Override
    default Page<MemberDTO> convertPageToDTO(Page<Member> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<MemberDTO> convertOptional(Optional<Member> member){
        return member.map(e -> convertToDTO(e));
    }
}
