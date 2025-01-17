package com.bootcamp.accountmanagement.repository.credit;

import com.bootcamp.accountmanagement.model.credit.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
    Mono<Credit> findCreditByCustomerId(String customerId);
}