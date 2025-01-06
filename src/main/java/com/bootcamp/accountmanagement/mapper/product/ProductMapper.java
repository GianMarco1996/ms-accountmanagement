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
        product.setType(getType(model.getType()));
        product.setCategory(getCategory(model.getCategory()));
        if (product.getCategory().equals("Tarjeta crédito")) {
            product.setTypeCreditCard(getTypeCreditCard(model.getTypeCreditCard()));
        }
        product.setDescription(model.getDescription());
        product.setName(Objects.nonNull(model.getName()) ? model.getName()
                : !product.getCategory().equals("Tarjeta crédito") ? product.getCategory() : product.getCategory().concat(" - ".concat(product.getTypeCreditCard())));
        return product;
    }

    private String getType(ProductRequest.TypeEnum typeEnum) {
        return switch (typeEnum) {
            case P -> "Pasivo";
            case A -> "Activo";
        };
    }

    private String getCategory(ProductRequest.CategoryEnum categoryEnum) {
        return switch (categoryEnum) {
            case A -> "Ahorro";
            case CC -> "Cuenta corriente";
            case PF -> "Plazo fijo";
            case P -> "Personal";
            case E -> "Empresarial";
            case TC -> "Tarjeta crédito";
        };
    }

    private String getTypeCreditCard(ProductRequest.TypeCreditCardEnum typeCreditCardEnum) {
        return switch (typeCreditCardEnum) {
            case P -> "Personal";
            case E -> "Empresarial";
        };
    }
}