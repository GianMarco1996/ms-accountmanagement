package com.bootcamp.accountmanagement.messaging;

import lombok.Data;

@Data
public class KafkaTransaction {

    private String category;

    private String type;

    private String mobile;

    private Double amount;

    private String transactionDate;

    private String description;

    private String debitCardId;
}