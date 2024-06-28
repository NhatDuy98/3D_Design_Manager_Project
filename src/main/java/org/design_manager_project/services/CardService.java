package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.card.CardDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.filters.CardMemberFilter;
import org.design_manager_project.mappers.CardMapper;
import org.design_manager_project.mappers.NotificationMapper;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.models.entity.Member;
import org.design_manager_project.models.entity.Notification;
import org.design_manager_project.models.enums.Status;
import org.design_manager_project.repositories.CardMemberRepository;
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
    private static final CardMapper cardMapper = CardMapper.INSTANCE;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private static final NotificationMapper notificationMapper = NotificationMapper.INSTANCE;
    private final NotificationRepository notificationRepository;
    private final CardMemberRepository cardMemberRepository;

    protected CardService(CardRepository cardRepository, CardMapper cardMapper, MemberRepository memberRepository, SimpMessageSendingOperations simpMessageSendingOperations, NotificationRepository notificationRepository, CardMemberRepository cardMemberRepository) {
        super(cardRepository, cardMapper);
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.notificationRepository = notificationRepository;
        this.cardMemberRepository = cardMemberRepository;
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
        var filter = new CardFilter();
        filter.setStatus(Status.IN_PROGRESS);
        filter.setDate(LocalDate.now());
        var cards = cardRepository.findAllCardWithStatusAndTime(filter);

        for (var e : cards){
            if (notificationRepository.existsByCardAndMember(e, e.getMember())){
                continue;
            }

            var notification = createNotification(e);
            notification.setContent("Card overdue!");
            notification.setMember(e.getMember());

            notificationRepository.save(notification);
            simpMessageSendingOperations.convertAndSendToUser(e.getMember().getUser().getEmail(),
                    "/topic/" + e.getProject().getId() + "/notifications",
                    notificationMapper.convertToDTO(notification)
            );
        }
    }

    public void sendNotificationForReview() {
        var filter = new CardFilter();
        filter.setStatus(Status.IN_REVIEW);
        filter.setDate(LocalDate.now());
        var cards = cardRepository.findAllCardWithStatusAndTime(filter);

        for (var e : cards){
            if (notificationRepository.existsByCardAndMember(e, e.getMember())){
                continue;
            }
            var notification = createNotification(e);
            notification.setContent("Need review soon");

            var filterCardMember = new CardMemberFilter();
            filterCardMember.setCardId(e.getId());
            var memberCards = cardMemberRepository.findAllWithFilter(filterCardMember.getPageable(),filterCardMember);
            if (!memberCards.isEmpty()){
                memberCards.forEach(i -> {
                    notification.setMember(i.getMember());
                    notificationRepository.save(notification);

                    simpMessageSendingOperations.convertAndSendToUser(i.getMember().getUser().getEmail(),
                            "/topic/" + e.getProject().getId() + "/notifications",
                            notificationMapper.convertToDTO(notification));
                });
            }
        }
    }
    private Notification createNotification(Card card){
        var notification = new Notification();
        notification.setCard(card);
        notification.setUrl("/cards/" + card.getId());
        notification.setIsRead(Boolean.FALSE);
        notification.setProject(card.getProject());
        return notification;
    }
}
