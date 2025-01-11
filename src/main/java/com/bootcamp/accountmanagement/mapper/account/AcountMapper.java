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
        account.setTypeCurrency(domain.getTypeCurrency());
        AccountResponseCustomer customer = new AccountResponseCustomer();
        customer.setId(domain.getCustomer().getId());
        customer.setType(domain.getCustomer().getType());
        account.setCustomer(customer);
        account.setProduct(productMapper.documentToModel(domain.getProduct()));
        if (Objects.nonNull(domain.getDebitCard())) {
            AccountResponseDebitCard debiCard = new AccountResponseDebitCard();
            debiCard.setId(domain.getDebitCard().getId());
            debiCard.setMainAccount(domain.getDebitCard().getMainAccount());
            account.setDebitCard(debiCard);
        }
        return account;
    }

    public AccountDetailResponse dtoToModel(AccountDTO dto) {
        AccountDetailResponse account = new AccountDetailResponse();
        account.setId(dto.getId());
        account.setAccountNumber(dto.getAccountNumber());
        account.setCurrentBalance(dto.getCurrentBalance());
        account.setOpeningDate(dto.getOpeningDate());
        account.setAccountStatus(dto.getAccountStatus());
        account.setTypeCurrency(dto.getTypeCurrency());
        AccountResponseCustomer customer = new AccountResponseCustomer();
        customer.setId(dto.getCustomer().getId());
        customer.setType(dto.getCustomer().getType());
        account.setCustomer(customer);
        account.setProduct(productMapper.documentToModel(dto.getProduct()));
        if (Objects.nonNull(dto.getDebitCard())) {
            AccountResponseDebitCard debiCard = new AccountResponseDebitCard();
            debiCard.setId(dto.getDebitCard().getId());
            debiCard.setMainAccount(dto.getDebitCard().getMainAccount());
            debiCard.setOrder(dto.getDebitCard().getOrder());
            account.setDebitCard(debiCard);
        }
        account.setTransactions(
                dto.getTransactions()
                        .stream()
                        .map(transaction -> transactionMapper.documentToModel(transaction))
                        .toList()
        );
        return account;
    }

    public Account modelToDocument(AccountRequest model) {
        Account account = new Account();
        account.setAccountNumber(model.getAccountNumber());
        account.setCurrentBalance(model.getCurrentBalance());
        account.setOpeningDate(model.getOpeningDate());
        account.setAccountStatus(getAccountStatus(model.getAccountStatus()));
        account.setTypeCurrency(getTypeCurrency(model.getTypeCurrency()));
        Customer customer = new Customer();
        customer.setId(model.getCustomer().getId());
        customer.setType(getTypeCustomer(model.getCustomer().getType()));
        account.setCustomer(customer);
        account.setProductId(model.getProductId());
        return account;
    }

    public AccountDTO documentToDto(Account document) {
        AccountDTO account = new AccountDTO();
        account.setId(document.getId());
        account.setAccountNumber(document.getAccountNumber());
        account.setCurrentBalance(document.getCurrentBalance());
        account.setOpeningDate(document.getOpeningDate());
        account.setAccountStatus(document.getAccountStatus());
        account.setTypeCurrency(document.getTypeCurrency());
        Customer customer = new Customer();
        customer.setId(document.getCustomer().getId());
        customer.setType(document.getCustomer().getType());
        account.setCustomer(customer);
        account.setProductId(document.getProductId());
        if (Objects.nonNull(document.getDebitCard())) {
            DebiCard debiCard = new DebiCard();
            debiCard.setId(document.getDebitCard().getId());
            debiCard.setMainAccount(document.getDebitCard().getMainAccount());
            debiCard.setOrder(document.getDebitCard().getOrder());
            account.setDebitCard(debiCard);
        }
        return account;
    }

    private String getTypeCurrency(AccountRequest.TypeCurrencyEnum typeCurrencyEnum) {
        return switch (typeCurrencyEnum) {
            case PEN -> "Soles";
            case USD -> "Dolares";
        };
    }

    private String getAccountStatus(AccountRequest.AccountStatusEnum accountStatusEnum) {
        return switch (accountStatusEnum) {
            case A -> "Activa";
            case I -> "Inactiva";
        };
    }

    private String getTypeCustomer(AccountRequestCustomer.TypeEnum typeCustomerEnum) {
        return switch (typeCustomerEnum) {
            case P -> "Personal";
            case E -> "Empresarial";
        };
    }
}