package com.bootcamp.accountmanagement.controller.account;

import com.bootcamp.accountmanagement.api.AccountApi;
import com.bootcamp.accountmanagement.mapper.account.AcountMapper;
import com.bootcamp.accountmanagement.model.AccountDetailResponse;
import com.bootcamp.accountmanagement.model.AccountRequest;
import com.bootcamp.accountmanagement.model.AccountResponse;
import com.bootcamp.accountmanagement.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Mono<ResponseEntity<AccountResponse>> getAccount(String id, ServerWebExchange exchange) {
        return accountService.getAccount(id)
                .map(account -> accountMapper.documentToModel(account))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AccountDetailResponse>> getAccountDetail(String id, ServerWebExchange exchange) {
        return accountService.getAccountDetail(id)
                .map(account -> accountMapper.dtoToModel(account))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<AccountResponse>>> getAccounts(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(accountService.getAccounts()
                .map(account -> accountMapper.documentToModel(account))));
    }

    @Override
    public Mono<ResponseEntity<Object>> registerAccount(Mono<AccountRequest> accountRequest, ServerWebExchange exchange) {
        return accountService.registerAccount(
                        accountRequest.map(account -> accountMapper.modelToDocument(account)))
                .map(ResponseEntity::ok);
                //.onErrorResume(Exception.class, e -> ResponseEntity.badRequest().body(e.getMessage()));
    }

    @Override
    public Mono<ResponseEntity<Void>> removeAccount(String id, ServerWebExchange exchange) {
        return accountService.removeAccount(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Object>> updateAccount(String id, Mono<AccountRequest> accountRequest, ServerWebExchange exchange) {
        return accountService.updateAccount(id,
                        accountRequest.map(account -> accountMapper.modelToDocument(account)))
                .map(ResponseEntity::ok);
    }
}