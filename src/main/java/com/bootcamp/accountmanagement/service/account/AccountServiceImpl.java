package com.bootcamp.accountmanagement.service.account;

import com.bootcamp.accountmanagement.mapper.account.AcountMapper;
import com.bootcamp.accountmanagement.model.AccountDebitCard;
import com.bootcamp.accountmanagement.model.BankTransfers;
import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import com.bootcamp.accountmanagement.model.account.DebiCard;
import com.bootcamp.accountmanagement.model.product.Product;
import com.bootcamp.accountmanagement.model.transaction.Transaction;
import com.bootcamp.accountmanagement.repository.account.AccountRepository;
import com.bootcamp.accountmanagement.repository.product.ProductRepository;
import com.bootcamp.accountmanagement.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AcountMapper mapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Flux<AccountDTO> getAccounts(String customerId)  {
        return accountRepository.findAll()
                .map(account -> mapper.documentToDto(account))
                .flatMap(account -> Objects.nonNull(customerId)
                        ? Flux.just(account).filter(accountDTO -> accountDTO.getCustomer().getId().equals(customerId))
                        : Flux.just(account)
                        )
                .flatMap(account -> Mono.just(account)
                        .zipWith(productRepository.findById(account.getProductId()), (a, p) -> {
                            a.setProduct(p);
                            return a;
                        }));
    }

    @Override
    public Mono<AccountDTO> getAccount(String id) {
        return accountRepository.findById(id)
                .map(account -> mapper.documentToDto(account))
                .flatMap(account -> Mono.just(account)
                        .zipWith(productRepository.findById(account.getProductId()), (a, p) -> {
                            a.setProduct(p);
                            return a;
                        }));
    }

    @Override
    public Mono<AccountDTO> getAccountTransactions(String id)  {
        return accountRepository.findById(id)
                .map(account -> mapper.documentToDto(account))
                .flatMap(account -> Mono.just(account)
                        .zipWith(productRepository.findById(account.getProductId()), (a, p) -> {
                            a.setProduct(p);
                            return a;
                        })
                        .zipWith(transactionRepository.findTransactionByAccountId(account.getId())
                                .collectList(), (a, t) -> {
                            a.setTransactions(t);
                            return a;
                        })
                );
    }

    @Override
    public Mono<Account> registerAccount(Mono<Account> account) {
        return account.flatMap(a -> productRepository.findById(a.getProductId())
                .filter(p -> p.getCategory().equals("Ahorro") || p.getCategory().equals("Cuenta corriente") || p.getCategory().equals("Plazo fijo"))
                .switchIfEmpty(Mono.error(new Exception("El id del producto no pertenece a una cuenta bancaria")))
                .flatMap(p -> {
                    Mono<Account> accountMono = Mono.empty();
                    if (a.getCustomer().getType().equals("Personal")) {
                        accountMono = registerAccountPersonal(a, p);
                    } else {
                        accountMono = registerAccountBusiness(a, p);
                    }
                    return accountMono;
                })
        );
    }

    @Override
    public Mono<Account> updateBalance(String id, double amount, boolean condition) {
        return accountRepository.findById(id)
                .map(a -> {
                    if (condition) {
                        a.setCurrentBalance(a.getCurrentBalance() + amount);
                    } else {
                        if (amount > a.getCurrentBalance()) {
                            throw new IllegalArgumentException("No tiene saldo suficiente para realizar la operación.");
                        }
                        a.setCurrentBalance(a.getCurrentBalance() - amount);
                    }
                    return a;
                })
                .flatMap(accountRepository::save);
    }

    @Override
    public Mono<Account> associateDebitCard(String id, Mono<AccountDebitCard> accountDebitCard) {
        return accountRepository.findById(id)
                .flatMap(account -> Objects.isNull(account.getDebitCard())
                        ? Mono.just(account)
                        : Mono.error(new Exception("La cuenta ya se encuentra asociada a una tarjeta de débito")))
                .flatMap(account -> updateDebitCard(account, accountDebitCard))
                .doOnNext(e -> e.setId(id))
                .flatMap(accountRepository::save);
    }

    @Override
    public Mono<String> updateAccountStatus(String id, String status) {
        return Mono.just(status)
                .filter(s -> s.equals("A") || s.equals("I"))
                .switchIfEmpty(Mono.error(new Exception("El estado ingresado es el incorrecto")))
                .flatMap(s -> accountRepository.findById(id)
                        .map(account -> {
                            if (s.equals("A")) {
                                account.setAccountStatus("Activa");
                            } else {
                                account.setAccountStatus("Inactiva");
                            }
                            return account;
                        })
                        .flatMap(accountRepository::save)
                        .flatMap(w -> status.equals("A")
                                ? Mono.just("Su cuenta bancaria fue activada exitosamente")
                                : Mono.just("Su cuenta bancaria fue desactivada"))
                );
    }

    @Override
    public Mono<String> bankTransfers(String id, Mono<BankTransfers> bankTransfers) {
        return bankTransfers.flatMap(bt -> accountRepository.findById(id)
                .flatMap(account -> {
                    if (account.getCurrentBalance() < bt.getAmount()) {
                        throw new IllegalArgumentException("No tiene saldo suficiente para realizar la operación.");
                    }
                    account.setCurrentBalance(account.getCurrentBalance() - bt.getAmount());
                    return accountRepository.save(account)
                            .flatMap(acc -> {
                                Transaction transaction = new Transaction();
                                transaction.setCategory("Transferencia");
                                transaction.setType("Movimiento");
                                transaction.setAccountId(account.getId());
                                transaction.setAmount(bt.getAmount());
                                transaction.setTransactionDate(LocalDate.now().toString());
                                transaction.setDescription("Transferencia realizada");
                                return transactionRepository.save(transaction)
                                        .map(savedTransaction -> acc);
                            });
                })
                .flatMap(account -> accountRepository.findAccountByAccountNumber(bt.getAccountNumber())
                        .flatMap(acc -> {
                            acc.setCurrentBalance(acc.getCurrentBalance() + bt.getAmount());
                            return accountRepository.save(acc)
                                    .flatMap(a -> {
                                        Transaction transaction = new Transaction();
                                        transaction.setCategory("Transferencia");
                                        transaction.setType("Movimiento");
                                        transaction.setAccountId(acc.getId());
                                        transaction.setAmount(bt.getAmount());
                                        transaction.setTransactionDate(LocalDate.now().toString());
                                        transaction.setDescription("Transferencia recibida");
                                        return transactionRepository.save(transaction)
                                                .map(savedTransaction -> a);
                                    });
                        })
                )



        ).flatMap(wallet -> Mono.just("Se realizo la transferencia correctamente"));
    }

    private Mono<Account> registerAccountPersonal(Account account, Product product) {
        return Mono.just(account)
                .flatMap(acc -> accountRepository.findAccountByCustomerId(acc.getCustomer().getId())
                        .any(ac -> acc.getProductId().equals(ac.getProductId()))
                        .flatMap(value ->
                                (value) ? Mono.just(product)
                                            .filter(p -> !(product.getCategory().equals("Ahorro") || product.getCategory().equals("Cuenta corriente")))
                                            .switchIfEmpty(Mono.error(new Exception("El cliente de tipo Persona ya cuenta con una cuenta bancaria de categoría ".concat(product.getCategory()))))
                                            .flatMap(p -> accountRepository.save(acc))
                                        : accountRepository.save(acc)));
    }

    private Mono<Account> registerAccountBusiness(Account account, Product product) {
        return Mono.just(account)
                .filter(acc -> !(product.getCategory().equals("Ahorro") || product.getCategory().equals("Plazo fijo")))
                .switchIfEmpty(Mono.error(new Exception("El cliente de tipo Empresarial no puede tener cuenta bancaria de categoría ".concat(product.getCategory()))))
                .flatMap(accountRepository::save);
    }

    private Mono<Account> updateDebitCard(Account account, Mono<AccountDebitCard> accountDebitCard) {
        return accountDebitCard.flatMap(dc ->
                accountRepository.findAccountByCustomerId(account.getCustomer().getId())
                        .filter(acc -> Objects.nonNull(acc.getDebitCard()))
                        .count()
                        .map(contador -> {
                            DebiCard debiCard = new DebiCard();
                            debiCard.setId(dc.getId());
                            debiCard.setMainAccount(dc.getMainAccount());
                            debiCard.setOrder((int) (contador + 1));
                            account.setDebitCard(debiCard);
                            return account;
                        })
        );
    }
}