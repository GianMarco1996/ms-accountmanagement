package com.bootcamp.accountmanagement.controller.account;

import com.bootcamp.accountmanagement.api.AccountApi;
import com.bootcamp.accountmanagement.mapper.account.AcountMapper;
import com.bootcamp.accountmanagement.model.AccountDebitCard;
import com.bootcamp.accountmanagement.model.AccountDetailResponse;
import com.bootcamp.accountmanagement.model.AccountRequest;
import com.bootcamp.accountmanagement.model.AccountResponse;
import com.bootcamp.accountmanagement.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountController implements AccountApi {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AcountMapper accountMapper;

    @Override
    public Mono<ResponseEntity<Object>> associateDebitCard(String id, Mono<AccountDebitCard> accountDebitCard, ServerWebExchange exchange) {
        return accountService.associateDebitCard(id, accountDebitCard)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AccountResponse>> getAccount(String id, ServerWebExchange exchange) {
        return accountService.getAccount(id)
                .map(account -> accountMapper.documentToModel(account))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AccountDetailResponse>> getAccountTransactions(String id, ServerWebExchange exchange) {
        return accountService.getAccountTransactions(id)
                .map(account -> accountMapper.dtoToModel(account))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<AccountResponse>>> getAccounts(String customerId, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(accountService.getAccounts(customerId)
                .map(account -> accountMapper.documentToModel(account))));
    }

    @Override
    public Mono<ResponseEntity<Object>> registerAccount(Mono<AccountRequest> accountRequest, ServerWebExchange exchange) {
        return accountService.registerAccount(
                        accountRequest.map(account -> accountMapper.modelToDocument(account)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Object>> updateAccountStatus(String id, String accountStatus, ServerWebExchange exchange) {
        return accountService.updateAccountStatus(id, accountStatus)
                .map(ResponseEntity::ok);
    }
}