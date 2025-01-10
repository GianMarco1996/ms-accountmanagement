package com.bootcamp.accountmanagement.service.card;

import com.bootcamp.accountmanagement.model.card.Card;
import com.bootcamp.accountmanagement.model.card.CardDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {
    Flux<CardDTO> getCards();

    Mono<CardDTO> getCard(String id);

    Mono<Card> registerCard(Mono<CardDTO> card);

    Mono<String> updateCardStatus(String id, String status);

    Mono<CardDTO> getCardTransactions(String id);
}