package com.bootcamp.accountmanagement.service.account;

import com.bootcamp.accountmanagement.mapper.account.AcountMapper;
import com.bootcamp.accountmanagement.model.AccountDebitCard;
import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import com.bootcamp.accountmanagement.model.account.DebiCard;
import com.bootcamp.accountmanagement.repository.account.AccountRepository;
import com.bootcamp.accountmanagement.repository.product.ProductRepository;
import com.bootcamp.accountmanagement.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return account.flatMap(ac -> {
            Mono<Account> accountMono = Mono.empty();
            if (ac.getCustomer().getType().equals("Personal")) {
                accountMono = registerAccountPersonal(ac);
            }
            if (ac.getCustomer().getType().equals("Empresarial")) {
                accountMono = registerAccountBusiness(ac);
            }
            return accountMono;
        });
    }

    @Override
    public Mono<Account> updateAccount(String id, Mono<Account> account) {
        return accountRepository.findById(id)
                .flatMap(a -> account)
                .doOnNext(e -> e.setId(id))
                .flatMap(accountRepository::save);
    }

    @Override
    public Mono<Void> removeAccount(String id)  {
        return accountRepository.deleteById(id);
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
                            //Mono.error(new Exception("No tiene saldo suficiente para realizar la operación."));
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
                .flatMap(account -> updateDebitCard(account, accountDebitCard))
                .doOnNext(e -> e.setId(id))
                .flatMap(accountRepository::save);
    }

    private Mono<Account> registerAccountPersonal(Account account) {
        return productRepository.findById(account.getProductId())
                .filter(product -> !(product.getCategory().equals("Empresarial") || (product.getCategory().equals("Tarjeta crédito") && product.getTypeCard().equals("Empresarial"))))
                .switchIfEmpty(Mono.error(new Exception("Un cliente Personal no puede tener las siguientes cuentas: en Activo Cuentas Empresarial" +
                        " ni tampoco una Tarjeta de crédito de tipo Empresarial")))
                .flatMap(product -> accountRepository.findAccountByCustomerId(account.getCustomer().getId())
                        .any(a -> account.getProductId().equals(a.getProductId()))
                        .flatMap(value ->
                                (value) ? Mono.just(product)
                                        .filter(p -> !(product.getCategory().equals("Ahorro") || product.getCategory().equals("Cuenta corriente") || product.getCategory().equals("Personal")
                                                || (product.getCategory().equals("Tarjeta crédito") && product.getTypeCard().equals("Personal"))))
                                        .switchIfEmpty(Mono.error(new Exception("Ya existe una cuenta con el producto ".concat(product.getCategory()))))
                                        .flatMap(p -> accountRepository.save(account))
                                        : accountRepository.save(account)));
    }

    private Mono<Account> registerAccountBusiness(Account account) {
        return productRepository.findById(account.getProductId())
                .filter(product -> !(product.getCategory().equals("Ahorro") || product.getCategory().equals("Plazo fijo")
                        || product.getCategory().equals("Personal") || (product.getCategory().equals("Tarjeta crédito") && product.getTypeCard().equals("Personal"))))
                .switchIfEmpty(Mono.error(new Exception("Un cliente Empresarial solo puede tener las siguientes cuentas: en Pasivo multiples Cuentas Corrientes," +
                        " Activo multiples cuentas empresariales y una Tarjeta de crédito")))
                .flatMap(product -> accountRepository.findAccountByCustomerId(account.getCustomer().getId())
                        .any(a -> account.getProductId().equals(a.getProductId()))
                        .flatMap(value ->
                                (value) ? Mono.just(product)
                                        .filter(p -> !p.getCategory().equals("Tarjeta crédito"))
                                        .switchIfEmpty(Mono.error(new Exception("Esta cuenta ya posee una tarjeta de crédito")))
                                        .flatMap(p -> accountRepository.save(account))
                                        : accountRepository.save(account)));
    }

    private Mono<Account> updateDebitCard(Account account, Mono<AccountDebitCard> accountDebitCard) {
        return accountDebitCard.flatMap(dc -> {
            DebiCard debiCard = new DebiCard();
            debiCard.setId(dc.getId());
            debiCard.setMainAccount(dc.getMainAccount());
            account.setDebitCard(debiCard);
            return Mono.just(account);
        });
    }
}