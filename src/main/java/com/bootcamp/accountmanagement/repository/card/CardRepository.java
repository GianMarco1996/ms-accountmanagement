package com.bootcamp.accountmanagement.repository.card;

import com.bootcamp.accountmanagement.model.card.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends ReactiveMongoRepository<Card, String> {
}