package com.bootcamp.accountmanagement.mapper.account;

import com.bootcamp.accountmanagement.mapper.product.ProductMapper;
import com.bootcamp.accountmanagement.mapper.transaction.TransactionMapper;
import com.bootcamp.accountmanagement.model.*;
import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import com.bootcamp.accountmanagement.model.account.Customer;
import com.bootcamp.accountmanagement.model.account.DebiCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AcountMapper {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    public AccountResponse documentToModel(AccountDTO domain) {
        AccountResponse account = new AccountResponse();
        account.setId(domain.getId());
        account.setAccountNumber(domain.getAccountNumber());
        account.setCurrentBalance(domain.getCurrentBalance());
        account.setOpeningDate(domain.getOpeningDate());
        account.setAccountStatus(domain.getAccountStatus());
        account.setProduct(productMapper.documentToModel(domain.getProduct()));
        AccountResponseCustomer customer = new AccountResponseCustomer();
        customer.setId(domain.getCustomer().getId());
        customer.setType(domain.getCustomer().getType());
        account.setCustomer(customer);
        account.setTypeCurrency(domain.getTypeCurrency());
        if (Objects.nonNull(domain.getDebitCard())) {
            AccountResponseDebitCard debiCard = new AccountResponseDebitCard();
            debiCard.setId(domain.getDebitCard().getId());
            debiCard.setMainAccount(domain.getDebitCard().getMainAccount());
            account.setDebitCard(debiCard);
        }
        account.setAmountApproved(domain.getAmountApproved());
        account.setInterestRate(domain.getInterestRate());
        account.setQuotas(domain.getQuotas());
        account.setPaymentDate(domain.getPaymentDate());
        account.setCreditLimit(domain.getCreditLimit());
        account.setExpirationDate(domain.getExpirationDate());
        return account;
    }

    public AccountDetailResponse dtoToModel(AccountDTO dto) {
        AccountDetailResponse account = new AccountDetailResponse();
        account.setId(dto.getId());
        account.setAccountNumber(dto.getAccountNumber());
        account.setCurrentBalance(dto.getCurrentBalance());
        account.setOpeningDate(dto.getOpeningDate());
        account.setAccountStatus(dto.getAccountStatus());
        account.setProduct(productMapper.documentToModel(dto.getProduct()));
        AccountResponseCustomer customer = new AccountResponseCustomer();
        customer.setId(dto.getCustomer().getId());
        customer.setType(dto.getCustomer().getType());
        account.setCustomer(customer);
        account.setTransactions(
                dto.getTransactions()
                        .stream()
                        .map(transaction -> transactionMapper.documentToModel(transaction))
                        .toList()
        );
        account.setTypeCurrency(dto.getTypeCurrency());
        if (Objects.nonNull(dto.getDebitCard())) {
            AccountResponseDebitCard debiCard = new AccountResponseDebitCard();
            debiCard.setId(dto.getDebitCard().getId());
            debiCard.setMainAccount(dto.getDebitCard().getMainAccount());
            account.setDebitCard(debiCard);
        }
        account.setAmountApproved(dto.getAmountApproved());
        account.setInterestRate(dto.getInterestRate());
        account.setQuotas(dto.getQuotas());
        account.setPaymentDate(dto.getPaymentDate());
        account.setCreditLimit(dto.getCreditLimit());
        account.setExpirationDate(dto.getExpirationDate());
        return account;
    }

    public Account modelToDocument(AccountRequest model) {
        Account account = new Account();
        account.setAccountNumber(model.getAccountNumber());
        account.setProductId(model.getProductId());
        account.setCurrentBalance(model.getCurrentBalance());
        account.setOpeningDate(model.getOpeningDate());
        account.setAccountStatus(model.getAccountStatus());
        Customer customer = new Customer();
        customer.setId(model.getCustomer().getId());
        customer.setType(model.getCustomer().getType());
        account.setCustomer(customer);
        account.setTypeCurrency(getTypeCurrency(model.getTypeCurrency()));
        if (Objects.nonNull(model.getDebitCard())) {
            DebiCard debiCard = new DebiCard();
            debiCard.setId(model.getDebitCard().getId());
            debiCard.setMainAccount(model.getDebitCard().getMainAccount());
            account.setDebitCard(debiCard);
        }
        account.setAmountApproved(model.getAmountApproved());
        account.setInterestRate(model.getInterestRate());
        account.setQuotas(model.getQuotas());
        account.setPaymentDate(model.getPaymentDate());
        account.setCreditLimit(model.getCreditLimit());
        account.setExpirationDate(model.getExpirationDate());
        return account;
    }

    public AccountDTO documentToDto(Account document) {
        AccountDTO account = new AccountDTO();
        account.setId(document.getId());
        account.setAccountNumber(document.getAccountNumber());
        account.setCurrentBalance(document.getCurrentBalance());
        account.setOpeningDate(document.getOpeningDate());
        account.setAccountStatus(document.getAccountStatus());
        account.setProductId(document.getProductId());
        Customer customer = new Customer();
        customer.setId(document.getCustomer().getId());
        customer.setType(document.getCustomer().getType());
        account.setCustomer(customer);
        account.setTypeCurrency(document.getTypeCurrency());
        if (Objects.nonNull(document.getDebitCard())) {
            DebiCard debiCard = new DebiCard();
            debiCard.setId(document.getDebitCard().getId());
            debiCard.setMainAccount(document.getDebitCard().getMainAccount());
            account.setDebitCard(debiCard);
        }
        account.setAmountApproved(document.getAmountApproved());
        account.setInterestRate(document.getInterestRate());
        account.setQuotas(document.getQuotas());
        account.setPaymentDate(document.getPaymentDate());
        account.setCreditLimit(document.getCreditLimit());
        account.setExpirationDate(document.getExpirationDate());
        return account;
    }

    private String getTypeCurrency(AccountRequest.TypeCurrencyEnum typeCurrencyEnum) {
        return switch (typeCurrencyEnum) {
            case PEN -> "Soles";
            case USD -> "Dolares";
        };
    }
}