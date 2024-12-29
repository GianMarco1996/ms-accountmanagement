package com.bootcamp.accountmanagement.service.account;

import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<Account> getAccounts();
    Mono<Account> getAccount(String id);
    Mono<AccountDTO> getAccountDetail(String id);
    Mono<Account> registerAccount(Mono<Account> account);
    Mono<Account> updateAccount(String id, Mono<Account> account);
    Mono<Void> removeAccount(String id);
    Mono<Account> updateBalance(String id, double amount, boolean condition);
}