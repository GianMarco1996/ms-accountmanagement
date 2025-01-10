package com.bootcamp.accountmanagement.service.card;

import com.bootcamp.accountmanagement.mapper.card.CardMapper;
import com.bootcamp.accountmanagement.model.AccountDebitCard;
import com.bootcamp.accountmanagement.model.card.Card;
import com.bootcamp.accountmanagement.model.card.CardDTO;
import com.bootcamp.accountmanagement.repository.card.CardRepository;
import com.bootcamp.accountmanagement.repository.product.ProductRepository;
import com.bootcamp.accountmanagement.repository.transaction.TransactionRepository;
import com.bootcamp.accountmanagement.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Flux<CardDTO> getCards() {
        return cardRepository.findAll()
                .map(cardMapper::documentToDto)
                .flatMap(card -> Mono.just(card)
                        .zipWith(productRepository.findById(card.getProductId()), (c, p) -> {
                            c.setProduct(p);
                            return c;
                        }));
    }

    @Override
    public Mono<CardDTO> getCard(String id) {
        return cardRepository.findById(id)
                .map(cardMapper::documentToDto)
                .flatMap(card -> Mono.just(card)
                        .zipWith(productRepository.findById(card.getProductId()), (c, p) -> {
                            c.setProduct(p);
                            return c;
                        }));
    }

    @Override
    public Mono<Card> registerCard(Mono<CardDTO> card) {
        return card.flatMap(c -> productRepository.findById(c.getProductId())
                .filter(p -> p.getCategory().equals("Tarjeta crédito") || p.getCategory().equals("Tarjeta débito"))
                .switchIfEmpty(Mono.error(new Exception("El id del producto no pertenece a una tarjeta de crédito o Tarjeta débito")))
                .flatMap(p -> Mono.just(cardMapper.dtoToDocument(c))
                        .flatMap(cardRepository::save)
                        .flatMap(ca -> p.getCategory().equals("Tarjeta crédito") ? Mono.just(ca) : updateAccount(c, ca.getId()))
                )
        );
    }

    @Override
    public Mono<String> updateCardStatus(String id, String status) {
        return Mono.just(status)
                .filter(s -> s.equals("A") || s.equals("B") || s.equals("C"))
                .switchIfEmpty(Mono.error(new Exception("El estado ingresado es el incorrecto")))
                .flatMap(s -> cardRepository.findById(id)
                        .map(card -> {
                            if (s.equals("A")) {
                                card.setCardStatus("Activa");
                            } else if (s.equals("B")) {
                                card.setCardStatus("Bloqueada");
                            } else {
                                card.setCardStatus("Caducada");
                            }
                            return card;
                        })
                        .flatMap(cardRepository::save)
                        .flatMap(w -> status.equals("A")
                                ? Mono.just("Su tarjeta fue activada exitosamente")
                                : status.equals("B") ? Mono.just("Su tarjeta fue Bloqueada")
                                : Mono.just("Su tarjeta fue Caducada"))
                );
    }

    @Override
    public Mono<CardDTO> getCardTransactions(String id) {
        return cardRepository.findById(id)
                .map(cardMapper::documentToDto)
                .flatMap(card -> Mono.just(card)
                        .zipWith(productRepository.findById(card.getProductId()), (c, p) -> {
                            c.setProduct(p);
                            return c;
                        })
                        .zipWith(transactionRepository.findTransactionByAccountId(card.getId())
                                .collectList(), (a, t) -> {
                            a.setTransactions(t);
                            return a;
                        }));
    }

    private Mono<Card> updateAccount(CardDTO card, String cardId) {
        return Mono.just(card).flatMap(c -> accountService.associateDebitCard(card.getAccountId(),
                        Mono.just(new AccountDebitCard().id(cardId).mainAccount(Boolean.TRUE)))
                .map(a -> cardMapper.dtoToDocument(c)));

    }
}