package org.design_manager_project.services;

import org.design_manager_project.dtos.space.SpaceDTO;
import org.design_manager_project.filter.SpaceFilter;
import org.design_manager_project.mappers.SpaceMapper;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.models.entity.Space;
import org.design_manager_project.models.enums.Role;
import org.design_manager_project.repositories.MemberRepository;
import org.design_manager_project.repositories.SpaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpaceService extends BaseService<Space, SpaceDTO, SpaceFilter, UUID>{

    private final SpaceRepository spaceRepository;
    private final SpaceMapper spaceMapper;
    private final MemberRepository memberRepository;
    protected SpaceService(SpaceRepository spaceRepository, SpaceMapper spaceMapper,MemberRepository memberRepository) {
        super(spaceRepository, spaceMapper);
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.memberRepository = memberRepository;
    }
    public Optional<SpaceDTO> getSpaceWithUserId(UUID userId){
        return spaceMapper.convertOptional(spaceRepository.findWithUserId(userId));
    }

    private Member updateInfoMember(Space space){
        Member member = new Member();
        member.setUser(space.getUser());
        member.setSpace(space);
        member.setRole(Role.HOST);
        return member;
    }


    @Override
    public SpaceDTO create(SpaceDTO dto) {
        Space space = spaceMapper.convertToEntity(dto);

        memberRepository.save(updateInfoMember(space));
        return spaceMapper.convertToDTO(space);
    }

    @Override
    public List<SpaceDTO> createAll(List<SpaceDTO> list) {
        List<Member> members = list.stream().map(e -> {
            Space space = spaceMapper.convertToEntity(e);

            return updateInfoMember(space);
        }).toList();

        memberRepository.saveAll(members);
        return spaceMapper.convertListToDTO(members.stream().map(e -> e.getSpace()).toList());
    }
}
