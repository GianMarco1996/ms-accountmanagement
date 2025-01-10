package com.bootcamp.accountmanagement.controller.credit;

import com.bootcamp.accountmanagement.api.CreditApi;
import com.bootcamp.accountmanagement.mapper.credit.CreditMapper;
import com.bootcamp.accountmanagement.model.CreditDetailResponse;
import com.bootcamp.accountmanagement.model.CreditRequest;
import com.bootcamp.accountmanagement.model.CreditResponse;
import com.bootcamp.accountmanagement.service.credit.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CreditController implements CreditApi {

    @Autowired
    private CreditService creditService;

    @Autowired
    private CreditMapper creditMapper;

    @Override
    public Mono<ResponseEntity<CreditResponse>> getCredit(String id, ServerWebExchange exchange) {
        return creditService.getCredit(id)
                .map(creditMapper::dtoToModel)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditDetailResponse>> getCreditTransactions(String id, ServerWebExchange exchange) {
        return creditService.getCreditTransactions(id)
                .map(creditMapper::dtoTOModelDetail)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditResponse>>> getCredits(String customerId, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(creditService.getCredits(customerId)
                .map(creditMapper::dtoToModel)));
    }

    @Override
    public Mono<ResponseEntity<Object>> registerCredit(Mono<CreditRequest> creditRequest, ServerWebExchange exchange) {
        return creditService.registerCredit(creditRequest.map(creditMapper::modelToDto))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Object>> updateCreditStatus(String id, String creditStatus, ServerWebExchange exchange) {
        return creditService.updateCreditStatus(id, creditStatus)
                .map(ResponseEntity::ok);
    }
}