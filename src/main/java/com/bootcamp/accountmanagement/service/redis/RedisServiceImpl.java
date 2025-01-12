package com.bootcamp.accountmanagement.service.redis;

import com.bootcamp.accountmanagement.model.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value(value = "${redis.key}")
    private String redisKey;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void registerTransactionsRedis(List<Transaction> transactions) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(transactions);
        redisTemplate.opsForValue().set(redisKey, jsonString);
    }
}