package com.bootcamp.accountmanagement.mapper.transaction;

import com.bootcamp.accountmanagement.model.TransactionRequest;
import com.bootcamp.accountmanagement.model.TransactionResponse;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse documentToModel(Transaction domain) {
        TransactionResponse transaction = new TransactionResponse();
        transaction.setId(domain.getId());
        transaction.setCategory(domain.getCategory());
        transaction.setType(domain.getType());
        transaction.setAccountId(domain.getAccountId());
        transaction.setAmount(domain.getAmount());
        transaction.setTransactionDate(domain.getTransactionDate());
        transaction.setDescription(domain.getDescription());
        return transaction;
    }

    public Transaction modelToDocument(TransactionRequest model) {
        Transaction transaction = new Transaction();
        String category = switch (model.getCategory()) {
            case D -> "Depósito";
            case R -> "Retiro";
            case C -> "Consumo";
        };
        transaction.setCategory(category);
        String type = switch (model.getType()) {
            case M -> "Movimiento";
            case P -> "Pago";
        };
        transaction.setType(type);
        transaction.setAccountId(model.getAccountId());
        transaction.setAmount(model.getAmount());
        transaction.setTransactionDate(model.getTransactionDate());
        transaction.setDescription(model.getDescription());
        return transaction;
    }
}