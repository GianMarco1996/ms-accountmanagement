package com.bootcamp.accountmanagement.service.product;

import com.bootcamp.accountmanagement.model.product.Product;
import com.bootcamp.accountmanagement.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Flux<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> registerProduct(Mono<Product> product) {
        return product.map(this::validateProduct)
                .flatMap(productRepository::insert);
    }

    @Override
    public Mono<Product> updateProduct(String id, Mono<Product> product) {
        return productRepository.findById(id)
                .flatMap(p -> product)
                .doOnNext(e -> e.setId(id))
                .flatMap(productRepository::save);
    }

    @Override
    public Mono<Void> removeProduct(String id) {
        return productRepository.deleteById(id);
    }

    private Product validateProduct(Product product) {
        if (product.getType().equals("Pasivo")) {
            if (product.getCategory().equals("Personal") || product.getCategory().equals("Empresarial")
                    || product.getCategory().equals("Tarjeta crédito")) {
                throw new IllegalArgumentException("Los productos que son de tipo Pasivo solo pueden tener la" +
                        " categoría de: Ahorro(A), Cuenta corriente(CC) y Plazo fijo(PF)");
            }
        } else {
            if (product.getCategory().equals("Ahorro") || product.getCategory().equals("Cuenta corriente")
                    || product.getCategory().equals("Plazo fijo")) {
                throw new IllegalArgumentException("Los productos que son de tipo Activo solo pueden tener la" +
                        " categoría de: Personal(P), Empresarial(E), Tarjeta crédito(TC)");
            }
            if (Objects.isNull(product.getTypeCard()) && product.getCategory().equals("Tarjeta crédito")) {
                throw new IllegalArgumentException("Los productos que son de tipo Activo y categoría Tarjeta crédito(TC)" +
                        " necesita enviar el tipo de tarjeta de crédito: Personal(P) y Empresarial(E)");
            }
        }
        return product;
    }
}