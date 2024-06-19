package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.card.CardDTO;
import org.design_manager_project.filters.CardFilter;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.services.CardService;
import org.design_manager_project.services.CommentService;
import org.design_manager_project.services.PrintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
public class CardController extends BaseController<Card, CardDTO, CardFilter, UUID> {
    private final CardService cardService;
    private final PrintService printService;
    private final CommentService commentService;
    protected CardController(CardService cardService, PrintService printService, CommentService commentService) {
        super(cardService);
        this.cardService = cardService;
        this.printService = printService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}/prints")
    public ResponseEntity<ApiResponse> getAllPrintsWithCard(
            @PathVariable("id") UUID cardID,
            PrintFilter printFilter
    ){
        return ResponseEntity.ok(ApiResponse.success(printService.getAllPrintsWithCard(cardID, printFilter)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse> getAllCommentsWithCard(
            @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(ApiResponse.success(commentService.getAllCommentsWithCard(id)));
    }

}
