package com.bootcamp.accountmanagement.repository.product;

import com.bootcamp.accountmanagement.model.product.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}