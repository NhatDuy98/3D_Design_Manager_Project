package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.project.ProjectDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filter.ProjectFilter;
import org.design_manager_project.mappers.ProjectMapper;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.models.entity.Project;
import org.design_manager_project.models.enums.Role;
import org.design_manager_project.repositories.MemberRepository;
import org.design_manager_project.repositories.ProjectRepository;
import org.design_manager_project.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.design_manager_project.utils.Constants.NOT_ACCEPTED;

@Service
public class ProjectService extends BaseService<Project, ProjectDTO, ProjectFilter, UUID> {

    private final ProjectRepository projectRepository;
    private static final ProjectMapper projectMapper = ProjectMapper.INSTANCE;
    private final MemberRepository memberRepository;
    protected ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, MemberRepository memberRepository) {
        super(projectRepository, projectMapper);
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    private void validateDate(LocalDate startDate, LocalDate endDate){
        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today) || endDate.isBefore(today)){
            throw new BadRequestException(NOT_ACCEPTED);
        }

        if (endDate.isBefore(startDate)){
            throw new BadRequestException(NOT_ACCEPTED);
        }

    }

    private List<Member> updateInfoMember(ProjectDTO projectDTO, Project project){
        Member member = memberRepository.findMemberWithSpaceAndUser(projectDTO.getSpace().getId(), projectDTO.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.DATA_NOT_FOUND));

        if (!member.getRole().equals(Role.HOST) && !member.getRole().equals(Role.MANAGER)){
            throw new BadRequestException(NOT_ACCEPTED);
        }

        member.setProject(project);

        List<Member> members = new ArrayList<>();
        members.add(member);

        return members;
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        validateDate(dto.getStartDate(), dto.getEndDate());

        Project project = projectMapper.convertToEntity(dto);

        project.setMembers(updateInfoMember(dto, project));

        projectRepository.save(project);

        return projectMapper.convertToDTO(project);
    }

    @Override
    public List<ProjectDTO> createAll(List<ProjectDTO> list) {
        List<Project> projects = list.stream().map(e -> {

            Project project = projectMapper.convertToEntity(e);

            validateDate(project.getStartDate(), project.getEndDate());

            project.setMembers(updateInfoMember(e, project));

            return project;
        }).toList();

        projectRepository.saveAll(projects);

        return projectMapper.convertListToDTO(projects);
    }

    @Override
    public ProjectDTO update(UUID uuid, ProjectDTO dto) {
        validateDate(dto.getStartDate(), dto.getEndDate());

        return super.update(uuid, dto);
    }

    @Override
    public List<ProjectDTO> updateAll(List<ProjectDTO> rqList) {
        rqList.forEach(e -> validateDate(e.getStartDate(), e.getEndDate()));

        return super.updateAll(rqList);
    }

    public Page<ProjectDTO> findAllProjectsWithSpace(ProjectFilter filter, UUID spaceId) {
        filter.setId(spaceId);

        return projectMapper.convertPageToDTO(projectRepository.findAllWithFilter(filter.getPageable(), filter));
    }
}
