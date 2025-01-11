package com.bootcamp.accountmanagement.mapper.credit;

import com.bootcamp.accountmanagement.mapper.product.ProductMapper;
import com.bootcamp.accountmanagement.mapper.transaction.TransactionMapper;
import com.bootcamp.accountmanagement.model.*;
import com.bootcamp.accountmanagement.model.account.Customer;
import com.bootcamp.accountmanagement.model.credit.Credit;
import com.bootcamp.accountmanagement.model.credit.CreditDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    public CreditDTO documentToDto(Credit document) {
        CreditDTO credit = new CreditDTO();
        credit.setId(document.getId());
        credit.setCreditNumber(document.getCreditNumber());
        credit.setCreditAmount(document.getCreditAmount());
        credit.setInterestRate(document.getInterestRate());
        credit.setTotalToPay(document.getTotalToPay());
        credit.setPendingAmount(document.getPendingAmount());
        credit.setLoanTerm(document.getLoanTerm());
        credit.setQuotaNumber(document.getQuotaNumber());
        credit.setQuotaPaid(document.getQuotaPaid());
        credit.setQuotaPending(document.getQuotaPending());
        credit.setMonthlyPayment(document.getMonthlyPayment());
        credit.setCreditStatus(document.getCreditStatus());
        credit.setTypeCurrency(document.getTypeCurrency());
        credit.setOpeningDate(document.getOpeningDate());
        credit.setExpirationDate(document.getExpirationDate());
        credit.setPaymentDate(document.getPaymentDate());
        credit.setCustomer(document.getCustomer());
        credit.setProductId(document.getProductId());
        return credit;
    }

    public CreditResponse dtoToModel(CreditDTO dto) {
        CreditResponse credit = new CreditResponse();
        credit.setId(dto.getId());
        credit.setCreditNumber(dto.getCreditNumber());
        credit.setCreditAmount(dto.getCreditAmount());
        credit.setInterestRate(dto.getInterestRate());
        credit.setTotalToPay(dto.getTotalToPay());
        credit.setPendingAmount(dto.getPendingAmount());
        credit.setLoanTerm(dto.getLoanTerm());
        credit.setQuotaNumber(dto.getQuotaNumber());
        credit.setQuotaPaid(dto.getQuotaPaid());
        credit.setQuotaPending(dto.getQuotaPending());
        credit.setMonthlyPayment(dto.getMonthlyPayment());
        credit.setCreditStatus(dto.getCreditStatus());
        credit.setTypeCurrency(dto.getTypeCurrency());
        credit.setOpeningDate(dto.getOpeningDate());
        credit.setExpirationDate(dto.getExpirationDate());
        credit.setPaymentDate(dto.getPaymentDate());
        CreditResponseCustomer customer = new CreditResponseCustomer();
        customer.setId(dto.getCustomer().getId());
        customer.setType(dto.getCustomer().getType());
        credit.setCustomer(customer);
        credit.setProduct(productMapper.documentToModel(dto.getProduct()));
        return credit;
    }

    public CreditDetailResponse dtoTOModelDetail(CreditDTO dto) {
        CreditDetailResponse credit = new CreditDetailResponse();
        credit.setId(dto.getId());
        credit.setCreditNumber(dto.getCreditNumber());
        credit.setCreditAmount(dto.getCreditAmount());
        credit.setInterestRate(dto.getInterestRate());
        credit.setTotalToPay(dto.getTotalToPay());
        credit.setPendingAmount(dto.getPendingAmount());
        credit.setLoanTerm(dto.getLoanTerm());
        credit.setQuotaNumber(dto.getQuotaNumber());
        credit.setQuotaPaid(dto.getQuotaPaid());
        credit.setQuotaPending(dto.getQuotaPending());
        credit.setMonthlyPayment(dto.getMonthlyPayment());
        credit.setCreditStatus(dto.getCreditStatus());
        credit.setTypeCurrency(dto.getTypeCurrency());
        credit.setOpeningDate(dto.getOpeningDate());
        credit.setExpirationDate(dto.getExpirationDate());
        credit.setPaymentDate(dto.getPaymentDate());
        CreditResponseCustomer customer = new CreditResponseCustomer();
        customer.setId(dto.getCustomer().getId());
        customer.setType(dto.getCustomer().getType());
        credit.setCustomer(customer);
        credit.setProduct(productMapper.documentToModel(dto.getProduct()));
        credit.setTransactions(
                dto.getTransactions()
                        .stream()
                        .map(transaction -> transactionMapper.documentToModel(transaction))
                        .toList()
        );
        return credit;
    }

    public Credit modelToDto(CreditRequest model) {
        Credit credit = new Credit();
        credit.setCreditNumber(model.getCreditNumber());
        credit.setCreditAmount(model.getCreditAmount());
        credit.setInterestRate(model.getInterestRate());
        credit.setTotalToPay(model.getCreditAmount() + (model.getCreditAmount() * model.getInterestRate()));
        credit.setPendingAmount(credit.getTotalToPay());
        credit.setLoanTerm(model.getLoanTerm());
        credit.setQuotaNumber(model.getLoanTerm());
        credit.setQuotaPaid(0);
        credit.setQuotaPending(model.getLoanTerm());
        credit.setMonthlyPayment(credit.getTotalToPay() / model.getLoanTerm());
        credit.setCreditStatus(getCreditStatus(model.getCreditStatus()));
        credit.setTypeCurrency(getTypeCurrency(model.getTypeCurrency()));
        credit.setOpeningDate(model.getOpeningDate());
        credit.setExpirationDate(model.getExpirationDate());
        credit.setPaymentDate(model.getPaymentDate());
        Customer customer = new Customer();
        customer.setId(model.getCustomer().getId());
        customer.setType(getCustomerType(model.getCustomer().getType()));
        credit.setCustomer(customer);
        credit.setProductId(model.getProductId());
        return credit;
    }

    private String getCreditStatus(CreditRequest.CreditStatusEnum creditStatusEnum) {
        return switch (creditStatusEnum) {
            case A -> "Activo";
            case P -> "Pagado";
        };
    }

    private String getTypeCurrency(CreditRequest.TypeCurrencyEnum typeCurrencyEnum) {
        return switch (typeCurrencyEnum) {
            case PEN -> "Soles";
            case USD -> "Dolares";
        };
    }

    private String getCustomerType(CreditRequestCustomer.TypeEnum customerTypeEnum) {
        return switch (customerTypeEnum) {
            case P -> "Personal";
            case E -> "Empresarial";
        };
    }
}