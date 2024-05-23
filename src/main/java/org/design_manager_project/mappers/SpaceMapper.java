package org.design_manager_project.mappers;

import org.design_manager_project.dtos.space.SpaceDTO;
import org.design_manager_project.models.entity.Space;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SpaceMapper extends BaseMapper<Space, SpaceDTO>{

    SpaceMapper INSTANCE = Mappers.getMapper(SpaceMapper.class);

    @Override
    Space convertToEntity(SpaceDTO spaceDTO);

    @Override
    default Page<SpaceDTO> convertPageToDTO(Page<Space> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<SpaceDTO> convertOptional(Optional<Space> space){
        return space.map(e -> convertToDTO(e));
    }
}
