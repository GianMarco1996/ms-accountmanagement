package com.bootcamp.accountmanagement.service.transaction;

import com.bootcamp.accountmanagement.model.transaction.Transaction;
import com.bootcamp.accountmanagement.repository.transaction.TransactionRepository;
import com.bootcamp.accountmanagement.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public Flux<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Transaction> getTransaction(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<Transaction> registerTransaction(Mono<Transaction> transaction) {
        /*return transaction.flatMap(t -> {
            Mono<Transaction> transactionMono = Mono.empty();
            if (t.getCategory().equals("Depósito")) {
                transactionMono = accountService.updateBalance(t.getAccountId(), t.getAmount(), true)
                        .flatMap(a -> transactionRepository.save(t));
            }
            if (t.getCategory().equals("Retiro")) {
                transactionMono = accountService.updateBalance(t.getAccountId(), t.getAmount(), false)
                        .flatMap(a -> transactionRepository.save(t));
            }
            return transactionMono;
        });*/
        return transaction.map(this::validateTransactionCategoryAndTypeForTheAccount)
                .flatMap(t -> {
            Mono<Transaction> transactionMono = Mono.empty();
            if (t.getType().equals("Movimiento")) {
                if (t.getCategory().equals("Depósito")) {
                    transactionMono = accountService.updateBalance(t.getAccountId(), t.getAmount(), true)
                            .flatMap(a -> transactionRepository.save(t));
                } else if (t.getCategory().equals("Retiro")) {
                    transactionMono = accountService.updateBalance(t.getAccountId(), t.getAmount(), false)
                            .flatMap(a -> transactionRepository.save(t));
                } else {
                    transactionMono = accountService.updateBalance(t.getAccountId(), t.getAmount(), false)
                            .flatMap(a -> transactionRepository.save(t));
                }
            } else {
                transactionMono = accountService.updateBalance(t.getAccountId(), t.getAmount(), true)
                        .flatMap(a -> transactionRepository.save(t));
            }
            return transactionMono;
        });
    }

    @Override
    public Mono<Transaction> updateTransaction(String id, Mono<Transaction> transaction) {
        return transactionRepository.findById(id)
                .flatMap(t -> transaction)
                .doOnNext(e -> e.setId(id))
                .flatMap(transactionRepository::save);
    }

    @Override
    public Mono<Void> removeTransaction(String id) {
        return transactionRepository.deleteById(id);
    }

    private Transaction validateTransactionCategoryAndTypeForTheAccount(Transaction transaction) {
        if (Objects.nonNull(transaction.getCategory())) {
            if (transaction.getType().equals("Pago")
                    && (transaction.getCategory().equals("Depósito")
                    || transaction.getCategory().equals("Retiro")
                    || transaction.getCategory().equals("Consumo"))) {
                throw new IllegalArgumentException("Los pagos a un crédito o una tarjeta de crédito no pueden tener categoría");
            }
        } else {
            if (transaction.getType().equals("Movimiento"))
                throw new IllegalArgumentException("Los movimientos tienen que tener una categoría");
        }
        return transaction;
    }
}