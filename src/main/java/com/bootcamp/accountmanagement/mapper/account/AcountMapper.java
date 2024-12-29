package com.bootcamp.accountmanagement.mapper.account;

import com.bootcamp.accountmanagement.mapper.product.ProductMapper;
import com.bootcamp.accountmanagement.mapper.transaction.TransactionMapper;
import com.bootcamp.accountmanagement.model.AccountDetailResponse;
import com.bootcamp.accountmanagement.model.AccountRequest;
import com.bootcamp.accountmanagement.model.AccountResponse;
import com.bootcamp.accountmanagement.model.AccountResponseCustomer;
import com.bootcamp.accountmanagement.model.account.Account;
import com.bootcamp.accountmanagement.model.account.AccountDTO;
import com.bootcamp.accountmanagement.model.account.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AcountMapper {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    public AccountResponse documentToModel(Account domain) {
        AccountResponse account = new AccountResponse();
        account.setId(domain.getId());
        account.setAccountNumber(domain.getAccountNumber());
        account.setProductId(domain.getProductId());
        account.setCurrentBalance(domain.getCurrentBalance());
        account.setOpeningDate(domain.getOpeningDate());
        account.setAccountStatus(domain.getAccountStatus());
        AccountResponseCustomer customer = new AccountResponseCustomer();
        customer.setId(domain.getCustomer().getId());
        customer.setType(domain.getCustomer().getType());
        account.setCustomer(customer);
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
        return account;
    }
}