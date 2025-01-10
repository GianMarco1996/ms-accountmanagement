package com.bootcamp.accountmanagement.service.account;

import com.bootcamp.accountmanagement.model.AccountDebitCard;
import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<AccountDTO> getAccounts(String customerId);

    Mono<AccountDTO> getAccount(String id);

    Mono<AccountDTO> getAccountTransactions(String id);

    Mono<Account> registerAccount(Mono<Account> account);

    Mono<Account> updateAccount(String id, Mono<Account> account);

    Mono<Void> removeAccount(String id);
    
    Mono<Account> updateBalance(String id, double amount, boolean condition);

    Mono<Account> associateDebitCard(String id, Mono<AccountDebitCard> accountDebitCard);
}