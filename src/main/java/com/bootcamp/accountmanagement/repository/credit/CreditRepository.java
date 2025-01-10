package com.bootcamp.accountmanagement.repository.credit;

import com.bootcamp.accountmanagement.model.credit.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
}