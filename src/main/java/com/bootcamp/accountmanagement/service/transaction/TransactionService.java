package com.bootcamp.accountmanagement.service.transaction;

import com.bootcamp.accountmanagement.messaging.KafkaTransaction;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionService {
    Flux<Transaction> getTransactions();

    Mono<Transaction> registerTransaction(Mono<Transaction> transaction);

    Mono<String> registerMessage(List<KafkaTransaction> transactions);
}