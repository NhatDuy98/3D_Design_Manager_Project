package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.member.MemberDTO;
import org.design_manager_project.dtos.user.response.UserOnlineDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.MemberFilter;
import org.design_manager_project.mappers.MemberMapper;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.models.entity.Project;
import org.design_manager_project.models.enums.Role;
import org.design_manager_project.repositories.MemberRepository;
import org.design_manager_project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.design_manager_project.utils.Constants.*;

@Service
public class MemberService extends BaseService<Member, MemberDTO, MemberFilter, UUID> {
    MemberMapper memberMapper = MemberMapper.INSTANCE;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final OnlOffService onlOffService;

    protected MemberService(MemberRepository memberRepository, MemberMapper memberMapper, ProjectRepository projectRepository, OnlOffService onlOffService) {
        super(memberRepository, memberMapper);
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.onlOffService = onlOffService;
    }

    private void checkDeleted(Member member){
        if (member.getDeletedAt() != null){
            throw new BadRequestException(OBJECT_DELETED);
        }

    }
    private void checkRole(UUID id, MemberDTO dto){
        Member memberCheckRole = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        if (memberCheckRole.getRole() == Role.HOST){
            throw new BadRequestException(BAD_REQUEST);
        }
    }
    private void checkAlreadyExists(MemberDTO dto){
        if (dto.getUser() != null && dto.getSpace() != null && dto.getProject() != null){
            Member member = memberRepository.findMemberWithUserAndProject(dto.getUser().getId(), dto.getProject().getId());

            if (member != null){
                throw new BadRequestException(OBJECT_ALREADY_EXISTS);
            }

            Project project = projectRepository.findById(dto.getProject().getId()).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

            if (!project.getSpace().getId().equals(dto.getSpace().getId())){
                throw new BadRequestException(BAD_REQUEST);
            }
        }
    }
    @Override
    public MemberDTO create(MemberDTO dto) {
        checkAlreadyExists(dto);

        return super.create(dto);
    }

    @Override
    public List<MemberDTO> createAll(List<MemberDTO> list) {

        list.forEach(e -> checkAlreadyExists(e));

        return super.createAll(list);
    }

    @Override
    public MemberDTO update(UUID uuid, MemberDTO dto) {
        checkRole(uuid, dto);

        return super.update(uuid, dto);
    }

    @Override
    public List<MemberDTO> updateAll(List<MemberDTO> rqList) {
        rqList.forEach(e -> checkRole(e.getId(), e));

        return super.updateAll(rqList);
    }

    @Override
    public void deleteById(UUID uuid) {

        Member member = memberRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        checkDeleted(member);

        member.setDeletedAt(Instant.now());

        memberRepository.save(member);
    }

    @Override
    public void deleteAll(List<UUID> uuids) {

        List<Member> members = memberRepository.findAllById(uuids).stream().map(e -> {
            checkDeleted(e);

            e.setDeletedAt(Instant.now());

            return e;
        }).toList();

        memberRepository.saveAll(members);
    }

    public List<UserOnlineDTO> getAllMembersOnlineWithProject(UUID projectId) {
        return onlOffService.getOnlineUsersWithProject(projectId);
    }
}
