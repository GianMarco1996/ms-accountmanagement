package com.bootcamp.accountmanagement.service.redis.product;

import com.bootcamp.accountmanagement.model.product.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ProductRedisService {
    void registerProductsRedis(List<Product> products) throws JsonProcessingException;
}