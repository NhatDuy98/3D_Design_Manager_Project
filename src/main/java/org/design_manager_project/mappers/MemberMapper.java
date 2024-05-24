package org.design_manager_project.mappers;

import org.design_manager_project.dtos.member.MemberDTO;
import org.design_manager_project.models.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MemberMapper extends BaseMapper<Member, MemberDTO>{
}
