package com.bootcamp.accountmanagement.controller.product;

import com.bootcamp.accountmanagement.api.ProductApi;
import com.bootcamp.accountmanagement.mapper.product.ProductMapper;
import com.bootcamp.accountmanagement.model.ProductRequest;
import com.bootcamp.accountmanagement.model.ProductResponse;
import com.bootcamp.accountmanagement.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductController implements ProductApi {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> getProducts(ServerWebExchange exchange) {
          return Mono.just(ResponseEntity.ok().body(productService.getProducts()
                          .map(product -> productMapper.documentToModel(product))));
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> getProduct(String id, ServerWebExchange exchange) {
        return productService.getProduct(id)
                .map(product -> productMapper.documentToModel(product))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Object>> registerProduct(Mono<ProductRequest> productRequest, ServerWebExchange exchange) {
        return productService.registerProduct(
                productRequest.map(product -> productMapper.modelToDocument(product)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Object>> updateProduct(String id, Mono<ProductRequest> productRequest, ServerWebExchange exchange) {
        return productService.updateProduct(id,
                        productRequest.map(product -> productMapper.modelToDocument(product)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> removeProduct(String id, ServerWebExchange exchange) {
        return productService.removeProduct(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}