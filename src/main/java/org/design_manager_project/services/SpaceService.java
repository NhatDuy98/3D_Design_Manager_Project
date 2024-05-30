package org.design_manager_project.services;

import org.design_manager_project.dtos.space.SpaceDTO;
import org.design_manager_project.filter.SpaceFilter;
import org.design_manager_project.mappers.SpaceMapper;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.models.entity.Space;
import org.design_manager_project.models.enums.Role;
import org.design_manager_project.repositories.SpaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SpaceService extends BaseService<Space, SpaceDTO, SpaceFilter, UUID>{

    private final SpaceRepository spaceRepository;
    private final SpaceMapper spaceMapper;
    protected SpaceService(SpaceRepository spaceRepository, SpaceMapper spaceMapper) {
        super(spaceRepository, spaceMapper);
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
    }
    public Page<SpaceDTO> getAllSpaceWithUserId(SpaceFilter filter, UUID userId){
        filter.setUserId(userId);
        return spaceMapper.convertPageToDTO(spaceRepository.findAllWithFilter(filter.getPageable() ,filter));
    }

    private List<Member> createMemberDefault(Space space){
        Member member = new Member();
        member.setUser(space.getUser());
        member.setSpace(space);
        member.setRole(Role.HOST);

        List<Member> members = new ArrayList<>();
        members.add(member);

        return members;
    }


    @Override
    public SpaceDTO create(SpaceDTO dto) {
        Space space = spaceMapper.convertToEntity(dto);

        space.setMembers(createMemberDefault(space));

        spaceRepository.save(space);
        return spaceMapper.convertToDTO(space);
    }

    @Override
    public List<SpaceDTO> createAll(List<SpaceDTO> list) {
        List<Space> spaces = list.stream().map(e -> {
            Space space = spaceMapper.convertToEntity(e);

            space.setMembers(createMemberDefault(space));

            return space;
        }).toList();

        spaceRepository.saveAll(spaces);
        return spaceMapper.convertListToDTO(spaces);
    }
}
