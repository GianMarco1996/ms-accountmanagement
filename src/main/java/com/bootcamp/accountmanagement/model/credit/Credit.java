package com.bootcamp.accountmanagement.model.credit;

import com.bootcamp.accountmanagement.model.account.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "credits")
public class Credit {
    @Id
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
}