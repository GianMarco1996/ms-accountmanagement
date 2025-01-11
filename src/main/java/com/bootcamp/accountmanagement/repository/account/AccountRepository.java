package com.bootcamp.accountmanagement.repository.account;

import com.bootcamp.accountmanagement.model.account.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Flux<Account> findAccountByCustomerId(String customerId);

    Mono<Account> findAccountByAccountNumber(String accountNumber);
}