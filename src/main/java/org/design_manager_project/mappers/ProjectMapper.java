package org.design_manager_project.mappers;

import org.design_manager_project.dtos.project.ProjectDTO;
import org.design_manager_project.models.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper extends BaseMapper<Project, ProjectDTO>{
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Override
    Project convertToEntity(ProjectDTO projectDTO);

    @Override
    ProjectDTO convertToDTO(Project entity);

    @Override
    default Page<ProjectDTO> convertPageToDTO(Page<Project> pageE){
        return pageE.map(e -> convertToDTO(e));
    }

    @Override
    default Optional<ProjectDTO> convertOptional(Optional<Project> project){
        return project.map(e -> convertToDTO(e));
    }
}
