package org.design_manager_project.controllers;

import org.design_manager_project.dtos.card.CardDTO;
import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.services.CardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
public class CardController extends BaseController<Card, CardDTO, CardFilter, UUID> {
    private final CardService cardService;
    protected CardController(CardService cardService) {
        super(cardService);
        this.cardService = cardService;
    }
}
