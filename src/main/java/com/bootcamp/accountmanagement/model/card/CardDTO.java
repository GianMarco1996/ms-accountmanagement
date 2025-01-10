package com.bootcamp.accountmanagement.model.card;

import com.bootcamp.accountmanagement.model.account.Customer;
import com.bootcamp.accountmanagement.model.product.Product;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CardDTO {
    private String id;

    private String cardNumber;

    private Integer cvv;

    private String openingDate;

    private String expirationDate;

    private String productId;

    private Product product;

    private Customer customer;

    private String typeCurrency;

    private String cardStatus;

    private Double creditLimit;

    private Double currentBalance;

    private Double interestRate;

    private Double minimumPayment;

    private List<Transaction> transactions = new ArrayList<>();

    private String accountId;
}