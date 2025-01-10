package com.bootcamp.accountmanagement.controller.card;

import com.bootcamp.accountmanagement.api.CardApi;
import com.bootcamp.accountmanagement.mapper.card.CardMapper;
import com.bootcamp.accountmanagement.model.CardDetailResponse;
import com.bootcamp.accountmanagement.model.CardRequest;
import com.bootcamp.accountmanagement.model.CardResponse;
import com.bootcamp.accountmanagement.service.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CardController implements CardApi {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardMapper cardMapper;

    @Override
    public Mono<ResponseEntity<CardResponse>> getCard(String id, ServerWebExchange exchange) {
        return cardService.getCard(id)
                .map(cardMapper::dtoToModel)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CardResponse>>> getCards(String cardType, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(cardService.getCards()
                .map(cardMapper::dtoToModel)));
    }

    @Override
    public Mono<ResponseEntity<Object>> registerCard(Mono<CardRequest> cardRequest, ServerWebExchange exchange) {
        return cardService.registerCard(cardRequest.map(cardMapper::modelToDto))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Object>> updateCardStatus(String id, String cardStatus, ServerWebExchange exchange) {
        return cardService.updateCardStatus(id, cardStatus)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CardDetailResponse>> getCardTransactions(String id, ServerWebExchange exchange) {
        return cardService.getCardTransactions(id)
                .map(cardMapper::dtoTOModelDetail)
                .map(ResponseEntity::ok);
    }
}