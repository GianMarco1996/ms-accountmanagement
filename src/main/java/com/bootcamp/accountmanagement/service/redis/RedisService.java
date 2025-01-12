package com.bootcamp.accountmanagement.service.redis;

import com.bootcamp.accountmanagement.model.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface RedisService {
    void registerTransactionsRedis(List<Transaction> transactions) throws JsonProcessingException;
}