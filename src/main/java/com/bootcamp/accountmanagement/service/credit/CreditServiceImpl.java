package com.bootcamp.accountmanagement.service.credit;

import com.bootcamp.accountmanagement.mapper.credit.CreditMapper;
import com.bootcamp.accountmanagement.model.credit.Credit;
import com.bootcamp.accountmanagement.model.credit.CreditDTO;
import com.bootcamp.accountmanagement.repository.credit.CreditRepository;
import com.bootcamp.accountmanagement.repository.product.ProductRepository;
import com.bootcamp.accountmanagement.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Flux<CreditDTO> getCredits(String customerId) {
        return creditRepository.findAll()
                .map(creditMapper::documentToDto)
                .flatMap(credit -> Objects.nonNull(customerId)
                        ? Flux.just(credit).filter(creditDTO -> creditDTO.getCustomer().getId().equals(customerId))
                        : Flux.just(credit)
                )
                .flatMap(credit -> Mono.just(credit)
                        .zipWith(productRepository.findById(credit.getProductId()), (c, p) -> {
                            c.setProduct(p);
                            return c;
                        }));
    }

    @Override
    public Mono<CreditDTO> getCredit(String id) {
        return creditRepository.findById(id)
                .map(creditMapper::documentToDto)
                .flatMap(credit -> Mono.just(credit)
                        .zipWith(productRepository.findById(credit.getProductId()), (c, p) -> {
                            c.setProduct(p);
                            return c;
                        }));
    }

    @Override
    public Mono<Credit> registerCredit(Mono<Credit> credit) {
        return credit.flatMap(c -> productRepository.findById(c.getProductId())
                .filter(p -> p.getCategory().equals("Personal") || p.getCategory().equals("Empresarial"))
                .switchIfEmpty(Mono.error(new Exception("El id del producto no pertenece a un crédito")))
                .flatMap(p -> creditRepository.save(c))
        );
    }

    @Override
    public Mono<String> updateCreditStatus(String id, String status) {
        return Mono.just(status)
                .filter(s -> s.equals("A") || s.equals("P"))
                .switchIfEmpty(Mono.error(new Exception("El estado ingresado es el incorrecto")))
                .flatMap(s -> creditRepository.findById(id)
                        .map(credit -> {
                            if (s.equals("A")) {
                                credit.setCreditStatus("Activo");
                            } else {
                                credit.setCreditStatus("Pagado");
                            }
                            return credit;
                        })
                        .flatMap(creditRepository::save)
                        .flatMap(w -> status.equals("A")
                                ? Mono.just("Su crédito fue activado exitosamente")
                                : Mono.just("Su crédito fue pagado"))
                );
    }

    @Override
    public Mono<CreditDTO> getCreditTransactions(String id) {
        return creditRepository.findById(id)
                .map(creditMapper::documentToDto)
                .flatMap(credit -> Mono.just(credit)
                        .zipWith(productRepository.findById(credit.getProductId()), (c, p) -> {
                            c.setProduct(p);
                            return c;
                        })
                        .zipWith(transactionRepository.findTransactionByAccountId(credit.getId())
                                .collectList(), (a, t) -> {
                            a.setTransactions(t);
                            return a;
                        }));
    }
}