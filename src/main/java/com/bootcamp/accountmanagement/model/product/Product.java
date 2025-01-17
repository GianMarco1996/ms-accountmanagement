package com.bootcamp.accountmanagement.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String name;

    private String type;

    private String category;

    private String typeCard;

    private String description;

    private Integer monthlyMovements;

    private Double commissionMaintenance;

    private Integer movementDay;

    private Double pricePurchase;

    private Double priceSale;
}