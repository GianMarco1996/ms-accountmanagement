package com.bootcamp.accountmanagement.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;

    private String accountNumber;

    private Double currentBalance;

    private String openingDate;

    private String accountStatus;

    private String typeCurrency;

    private Customer customer;

    private String productId;

    private DebiCard debitCard;
}