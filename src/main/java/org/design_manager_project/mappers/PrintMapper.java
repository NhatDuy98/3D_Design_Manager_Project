package org.design_manager_project.mappers;

import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.models.entity.Print;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrintMapper extends BaseMapper<Print, PrintDTO> {
    PrintMapper INSTANCE = Mappers.getMapper(PrintMapper.class);

    @Override
    @Mapping(source = "image", target = "image", ignore = true)
    Print convertToEntity(PrintDTO printDTO);

    @Override
    @Mapping(source = "versions", target = "images")
    PrintDTO convertToDTO(Print entity);

    @Override
    @Mapping(source = "image", target = "image", ignore = true)
    Print updateEntity(PrintDTO printDTO,@MappingTarget Print entity);

    @Override
    default Page<PrintDTO> convertPageToDTO(Page<Print> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<PrintDTO> convertOptional(Optional<Print> print){
        return print.map(e -> convertToDTO(e));
    }
}
