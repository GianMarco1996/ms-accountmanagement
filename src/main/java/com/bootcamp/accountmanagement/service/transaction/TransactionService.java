package com.bootcamp.accountmanagement.service.transaction;

import com.bootcamp.accountmanagement.model.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Flux<Transaction> getTransactions();

    Mono<Transaction> registerTransaction(Mono<Transaction> transaction);
}