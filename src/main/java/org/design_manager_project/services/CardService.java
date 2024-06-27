package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.card.CardDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.mappers.CardMapper;
import org.design_manager_project.mappers.NotificationMapper;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.models.entity.Notification;
import org.design_manager_project.models.enums.Status;
import org.design_manager_project.repositories.CardRepository;
import org.design_manager_project.repositories.MemberRepository;
import org.design_manager_project.repositories.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.design_manager_project.utils.Constants.*;


@Service
public class CardService extends BaseService<Card, CardDTO, CardFilter, UUID>{

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final CardMapper cardMapper = CardMapper.INSTANCE;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final NotificationMapper notificationMapper = NotificationMapper.INSTANCE;
    private final NotificationRepository notificationRepository;


    protected CardService(CardRepository cardRepository, CardMapper cardMapper, MemberRepository memberRepository, SimpMessageSendingOperations simpMessageSendingOperations, NotificationRepository notificationRepository) {
        super(cardRepository, cardMapper);
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.notificationRepository = notificationRepository;
    }

    private void validateDeleted(Card card){
        if (card.getDeletedAt() != null){
            throw new BadRequestException(OBJECT_DELETED);
        }
    }
    private void validateMemberAndProject(CardDTO dto){
        Member member = memberRepository.findById(dto.getMember().getId()).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        if (!member.getProject().getId().equals(dto.getProject().getId())){
            throw new BadRequestException(BAD_REQUEST);
        }
    }

    private void validateDate(CardDTO dto){
        LocalDate today = LocalDate.now();
        if (dto.getStartDate().isBefore(today) || dto.getEndDate().isBefore(today)){
            throw new BadRequestException(NOT_ACCEPTED);
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())){
            throw new BadRequestException(NOT_ACCEPTED);
        }
    }

    @Override
    public CardDTO create(CardDTO dto) {
        validateMemberAndProject(dto);
        validateDate(dto);

        if (dto.getStatus() == null){
            dto.setStatus(String.valueOf(Status.OPENED));
        }

        return super.create(dto);
    }

    @Override
    public List<CardDTO> createAll(List<CardDTO> list) {
        list.forEach(e -> {
            validateMemberAndProject(e);
            validateDate(e);

            if (e.getStatus() == null){
                e.setStatus(String.valueOf(Status.OPENED));
            }
        });

        return super.createAll(list);
    }

    @Override
    public void deleteById(UUID uuid) {
        Card card = cardRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        validateDeleted(card);

        card.setDeletedAt(Instant.now());

        cardRepository.save(card);
    }

    @Override
    public void deleteAll(List<UUID> uuids) {

        List<Card> cards = cardRepository.findAllById(uuids).stream().map(e -> {
            validateDeleted(e);

            e.setDeletedAt(Instant.now());

            return e;
        }).toList();

        cardRepository.saveAll(cards);
    }

    public Page<CardDTO> getAllCardsWithProject(UUID projectId, CardFilter filter) {
        filter.setProjectId(projectId);

        return cardMapper.convertPageToDTO(cardRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    public void sendNotificationOverDueCard() {
        List<Card> cards = cardRepository.findAllCardInProgressAndOverdue(Status.IN_PROGRESS, LocalDate.now());

        for (var e : cards){
            if (notificationRepository.existsByCard(e)){
                continue;
            }

            var notification = new Notification();
            notification.setCard(e);
            notification.setUrl("/cards/" + e.getId());
            notification.setContent("Card overdue!");
            notification.setIsRead(Boolean.FALSE);
            notification.setMember(e.getMember());
            notification.setProject(e.getProject());
            notificationRepository.save(notification);
            simpMessageSendingOperations.convertAndSendToUser(e.getMember().getUser().getEmail(),
                    "/topic/" + e.getProject().getId() + "/notifications",
                    notificationMapper.convertToDTO(notification)
            );
        }
    }
}
