package com.bootcamp.accountmanagement.service.credit;

import com.bootcamp.accountmanagement.model.credit.Credit;
import com.bootcamp.accountmanagement.model.credit.CreditDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Flux<CreditDTO> getCredits(String customerId);

    Mono<CreditDTO> getCredit(String id);

    Mono<Credit> registerCredit(Mono<Credit> credit);

    Mono<String> updateCreditStatus(String id, String status);

    Mono<CreditDTO> getCreditTransactions(String id);
}