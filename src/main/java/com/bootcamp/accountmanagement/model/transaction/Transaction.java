package com.bootcamp.accountmanagement.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String category;
    private String type;
    private String accountId;
    private Double amount;
    private String transactionDate;
    private String description;
}