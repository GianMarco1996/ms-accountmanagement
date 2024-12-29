package com.bootcamp.accountmanagement.mapper.product;

import com.bootcamp.accountmanagement.model.ProductRequest;
import com.bootcamp.accountmanagement.model.ProductResponse;
import com.bootcamp.accountmanagement.model.product.Product;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductMapper {

    public ProductResponse documentToModel(Product domain) {
        ProductResponse product = new ProductResponse();
        product.setId(domain.getId());
        product.setName(domain.getName());
        product.setType(domain.getType());
        product.setCategory(domain.getCategory());
        product.setTypeCreditCard(domain.getTypeCreditCard());
        product.setDescription(domain.getDescription());
        return product;
    }

    public Product modelToDocument(ProductRequest model) {
        Product product = new Product();
        String type = switch (model.getType()) {
            case P -> "Pasivo";
            case A -> "Activo";
        };
        product.setType(type);
        String category = switch (model.getCategory()) {
            case A -> "Ahorro";
            case CC -> "Cuenta corriente";
            case PF -> "Plazo fijo";
            case P -> "Personal";
            case E -> "Empresarial";
            case TC -> "Tarjeta crédito";
        };
        product.setCategory(category);
        if (product.getCategory().equals("Tarjeta crédito")) {
            String typeCreditCard = switch (model.getTypeCreditCard()) {
                case P -> "Personal";
                case E -> "Empresarial";
            };
            product.setTypeCreditCard(typeCreditCard);
        }
        product.setDescription(model.getDescription());
        product.setName(Objects.nonNull(model.getName()) ? model.getName()
                : !product.getCategory().equals("Tarjeta crédito") ? product.getCategory() : product.getCategory().concat(" - ".concat(product.getTypeCreditCard())));
        return product;
    }
}