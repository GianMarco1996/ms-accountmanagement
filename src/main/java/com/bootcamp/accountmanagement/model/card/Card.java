package com.bootcamp.accountmanagement.model.card;

import com.bootcamp.accountmanagement.model.account.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cards")
public class Card {
    @Id
    private String id;

    private String cardNumber;

    private Integer cvv;

    private String openingDate;

    private String expirationDate;

    private String productId;

    private Customer customer;

    private String typeCurrency;

    private String cardStatus;

    private Double creditLimit;

    private Double currentBalance;

    private Double interestRate;

    private Integer paymentDate;
}