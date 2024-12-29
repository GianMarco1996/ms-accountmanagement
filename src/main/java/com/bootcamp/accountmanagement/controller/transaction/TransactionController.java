package com.bootcamp.accountmanagement.controller.transaction;

import com.bootcamp.accountmanagement.api.TransactionApi;
import com.bootcamp.accountmanagement.mapper.transaction.TransactionMapper;
import com.bootcamp.accountmanagement.model.TransactionRequest;
import com.bootcamp.accountmanagement.model.TransactionResponse;
import com.bootcamp.accountmanagement.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TransactionController implements TransactionApi {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public Mono<ResponseEntity<TransactionResponse>> getTransaction(String id, ServerWebExchange exchange) {
        return transactionService.getTransaction(id)
                .map(account -> transactionMapper.documentToModel(account))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getTransactions(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(transactionService.getTransactions()
                .map(transaction -> transactionMapper.documentToModel(transaction))));
    }

    @Override
    public Mono<ResponseEntity<Object>> registerTransaction(Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return transactionService.registerTransaction(
                        transactionRequest.map(transaction -> transactionMapper.modelToDocument(transaction)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> removeTransaction(String id, ServerWebExchange exchange) {
        return transactionService.removeTransaction(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Object>> updateTransaction(String id, Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return transactionService.updateTransaction(id,
                        transactionRequest.map(transaction -> transactionMapper.modelToDocument(transaction)))
                .map(ResponseEntity::ok);
    }
}