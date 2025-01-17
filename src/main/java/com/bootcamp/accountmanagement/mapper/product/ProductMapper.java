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
        product.setTypeCard(domain.getTypeCard());
        product.setDescription(domain.getDescription());
        product.setMonthlyMovements(domain.getMonthlyMovements());
        product.setCommissionMaintenance(domain.getCommissionMaintenance());
        product.setMovementDay(domain.getMovementDay());
        product.setPricePurchase(domain.getPricePurchase());
        product.setPriceSale(domain.getPriceSale());
        return product;
    }

    public Product modelToDocument(ProductRequest model) {
        Product product = new Product();
        product.setType(getType(model.getType()));
        product.setCategory(getCategory(model.getCategory()));
        if (product.getCategory().equals("Tarjeta crédito") || product.getCategory().equals("Tarjeta débito")) {
            product.setTypeCard(getTypeCreditCard(model.getTypeCard()));
        }
        product.setDescription(model.getDescription());
        product.setName(Objects.nonNull(model.getName()) ? model.getName()
                : !(product.getCategory().equals("Tarjeta crédito") || product.getCategory().equals("Tarjeta débito")) ? product.getCategory() : product.getCategory().concat(" - ".concat(product.getTypeCard())));
        product.setMonthlyMovements(model.getMonthlyMovements());
        product.setCommissionMaintenance(model.getCommissionMaintenance());
        product.setMovementDay(model.getMovementDay());
        product.setPricePurchase(model.getPricePurchase());
        product.setPriceSale(model.getPriceSale());
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
            case TD -> "Tarjeta débito";
            case B -> "BootCoin";
        };
    }

    private String getTypeCreditCard(ProductRequest.TypeCardEnum typeCardEnum) {
        return switch (typeCardEnum) {
            case P -> "Personal";
            case E -> "Empresarial";
        };
    }
}