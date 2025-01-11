package com.bootcamp.accountmanagement.model.account;

import com.bootcamp.accountmanagement.model.product.Product;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDTO {
    private String id;

    private String accountNumber;

    private Double currentBalance;

    private String openingDate;

    private String accountStatus;

    private String typeCurrency;

    private Customer customer;

    private String productId;

    private Product product;

    private DebiCard debitCard;

    private List<Transaction> transactions = new ArrayList<>();
}