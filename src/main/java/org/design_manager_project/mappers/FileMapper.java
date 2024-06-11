package org.design_manager_project.mappers;

import org.design_manager_project.dtos.file.response.FileResponse;
import org.design_manager_project.models.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FileMapper extends BaseMapper<File, FileResponse>{
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Override
    default Page<FileResponse> convertPageToDTO(Page<File> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<FileResponse> convertOptional(Optional<File> file){
        return file.map(e -> convertToDTO(e));
    }
}
