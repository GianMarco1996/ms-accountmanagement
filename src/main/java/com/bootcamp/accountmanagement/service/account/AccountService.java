package com.bootcamp.accountmanagement.service.account;

import com.bootcamp.accountmanagement.model.AccountDebitCard;
import com.bootcamp.accountmanagement.model.BankTransfers;
import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<AccountDTO> getAccounts(String customerId);

    Mono<AccountDTO> getAccount(String id);

    Mono<AccountDTO> getAccountTransactions(String id);

    Mono<Account> registerAccount(Mono<Account> account);
    
    Mono<Account> updateBalance(String id, double amount, boolean condition);

    Mono<Account> associateDebitCard(String id, Mono<AccountDebitCard> accountDebitCard);

    Mono<String> updateAccountStatus(String id, String status);

    Mono<String> bankTransfers(String id, Mono<BankTransfers> bankTransfers);
}