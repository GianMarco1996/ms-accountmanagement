package com.bootcamp.accountmanagement.repository.transaction;


import com.bootcamp.accountmanagement.model.transaction.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findTransactionByAccountId(String accountId);
}