package org.design_manager_project.services;

import org.design_manager_project.dtos.card_member.CardMemberDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.CardMemberFilter;
import org.design_manager_project.mappers.CardMemberMapper;
import org.design_manager_project.models.entity.CardMember;
import org.design_manager_project.repositories.CardMemberRepository;
import org.design_manager_project.repositories.CardRepository;
import org.design_manager_project.repositories.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.design_manager_project.utils.Constants.*;

@Service
public class CardMemberService extends BaseService<CardMember, CardMemberDTO, CardMemberFilter, UUID>{
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final CardMemberRepository cardMemberRepository;
    private static final CardMemberMapper mapper = CardMemberMapper.INSTANCE;

    protected CardMemberService(CardMemberRepository cardMemberRepository, CardMemberMapper cardMemberMapper, CardRepository cardRepository, MemberRepository memberRepository) {
        super(cardMemberRepository, cardMemberMapper);
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
        this.cardMemberRepository = cardMemberRepository;
    }

    private void validateCardMember(UUID cardId, UUID memberId){
        var card = cardRepository.findById(cardId).orElseThrow(() -> new BadRequestException(DATA_NOT_FOUND));
        var member = memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(DATA_NOT_FOUND));

        if (card.getDeletedAt() != null || member.getDeletedAt() != null){
            throw new BadRequestException(OBJECT_DELETED);
        }

        if (!card.getProject().getId().equals(member.getProject().getId())){
            throw new BadRequestException(BAD_REQUEST);
        }

        if (cardMemberRepository.existsByCardAndMember(card, member)){
            throw new BadRequestException(DUPLICATE);
        }
    }

    @Override
    public CardMemberDTO create(CardMemberDTO dto) {
        validateCardMember(dto.getCard().getId(), dto.getMember().getId());
        return super.create(dto);
    }

    @Override
    public List<CardMemberDTO> createAll(List<CardMemberDTO> list) {
        list.forEach(e -> validateCardMember(e.getCard().getId(), e.getMember().getId()));
        return super.createAll(list);
    }

    public Page<CardMemberDTO> getAllMembersWithCard(UUID cardID) {
        var filter = new CardMemberFilter();
        filter.setCardId(cardID);
        var cardMember = cardMemberRepository.findAllWithFilter(filter.getPageable(), filter);
        return mapper.convertPageToDTO(cardMember);
    }
}
