package com.bootcamp.accountmanagement.service.redis.product;

import com.bootcamp.accountmanagement.model.product.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRedisServiceImpl implements ProductRedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value(value = "${redis.product-key}")
    private String redisKey;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void registerProductsRedis(List<Product> products) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(products);
        redisTemplate.opsForValue().set(redisKey, jsonString);
    }
}