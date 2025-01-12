package com.bootcamp.accountmanagement.service.transaction;

import com.bootcamp.accountmanagement.messaging.KafkaTransaction;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import com.bootcamp.accountmanagement.repository.transaction.TransactionRepository;
import com.bootcamp.accountmanagement.service.account.AccountService;
import com.bootcamp.accountmanagement.service.redis.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RedisService redisService;

    @Override
    public Flux<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Transaction> registerTransaction(Mono<Transaction> transaction) {
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
    public Mono<String> registerMessage(List<KafkaTransaction> transactions) {
        return Mono.when(transactions.stream()
                        .map(tra -> {
                            if (Objects.isNull(tra.getDebitCardId())) {
                                return saveTransaction(getKafkaModelToDocument(tra));
                            } else {
                                return Mono.just(getKafkaModelToDocument(tra));
                            }
                        })
                        .collect(Collectors.toList())
        ).then(Mono.just("Yanki realizado"));
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
            if (transaction.getType().equals("Movimiento")) {
                throw new IllegalArgumentException("Los movimientos tienen que tener una categoría");
            }
        }
        return transaction;
    }

    private Transaction getKafkaModelToDocument(KafkaTransaction kafkaTransaction) {
        Transaction transaction = new Transaction();
        transaction.setCategory(kafkaTransaction.getCategory());
        transaction.setType(kafkaTransaction.getType());
        transaction.setAmount(kafkaTransaction.getAmount());
        transaction.setTransactionDate(kafkaTransaction.getTransactionDate());
        transaction.setDescription(kafkaTransaction.getDescription());
        transaction.setMobile(kafkaTransaction.getMobile());
        return transaction;
    }

    private Mono<Transaction> saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction)
                .flatMap(tra -> transactionRepository.findAll()
                                .filter(t -> Objects.nonNull(t.getMobile()))
                                .collectList()
                                .map(t -> {
                                    registerRedis(t);
                                    return tra;
                                })
                );
    }

    private void registerRedis(List<Transaction> transactions) {
        try {
            redisService.registerTransactionsRedis(transactions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}