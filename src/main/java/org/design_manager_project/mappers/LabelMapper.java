package org.design_manager_project.mappers;

import org.design_manager_project.dtos.label.LabelDTO;
import org.design_manager_project.models.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LabelMapper extends BaseMapper<Label, LabelDTO>{
    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);

    @Override
    default Page<LabelDTO> convertPageToDTO(Page<Label> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<LabelDTO> convertOptional(Optional<Label> label){
        return label.map(e -> convertToDTO(e));
    }
}
