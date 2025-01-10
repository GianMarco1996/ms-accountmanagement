package com.bootcamp.accountmanagement.model.credit;

import com.bootcamp.accountmanagement.model.account.Customer;
import com.bootcamp.accountmanagement.model.product.Product;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreditDTO {
    private String id;

    private String creditNumber;

    private Double creditAmount;

    private Double interestRate;

    private Double totalToPay;

    private Double pendingAmount;

    private Integer loanTerm;

    private Integer quotaNumber;

    private Integer quotaPaid;

    private Integer quotaPending;

    private Double monthlyPayment;

    private String creditStatus;

    private String typeCurrency;

    private String openingDate;

    private String expirationDate;

    private Integer paymentDate;

    private Customer customer;

    private String productId;

    private Product product;

    private List<Transaction> transactions = new ArrayList<>();
}