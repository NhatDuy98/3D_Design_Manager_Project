package org.design_manager_project.mappers;

import org.design_manager_project.dtos.label_print.LabelPrintDTO;
import org.design_manager_project.models.entity.LabelPrint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LabelPrintMapper extends BaseMapper<LabelPrint, LabelPrintDTO> {
    LabelPrintMapper INSTANCE = Mappers.getMapper(LabelPrintMapper.class);

    @Override
    @Mapping(source = "XCoordinate", target = "x")
    @Mapping(source = "YCoordinate", target = "y")
    @Mapping(source = "ZCoordinate", target = "z")
    LabelPrintDTO convertToDTO(LabelPrint entity);

    @Override
    @Mapping(source = "x", target = "XCoordinate")
    @Mapping(source = "y", target = "YCoordinate")
    @Mapping(source = "z", target = "ZCoordinate")
    LabelPrint convertToEntity(LabelPrintDTO labelPrintDTO);

    @Override
    default Page<LabelPrintDTO> convertPageToDTO(Page<LabelPrint> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<LabelPrintDTO> convertOptional(Optional<LabelPrint> labelPrint){
        return labelPrint.map(e -> convertToDTO(e));
    }
}
