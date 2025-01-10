package com.bootcamp.accountmanagement.mapper.card;

import com.bootcamp.accountmanagement.mapper.product.ProductMapper;
import com.bootcamp.accountmanagement.mapper.transaction.TransactionMapper;
import com.bootcamp.accountmanagement.model.*;
import com.bootcamp.accountmanagement.model.account.Customer;
import com.bootcamp.accountmanagement.model.card.Card;
import com.bootcamp.accountmanagement.model.card.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CardMapper {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    public CardDTO documentToDto(Card document) {
        CardDTO card = new CardDTO();
        card.setId(document.getId());
        card.setCardNumber(document.getCardNumber());
        card.setCvv(document.getCvv());
        card.setOpeningDate(document.getOpeningDate());
        card.setExpirationDate(document.getExpirationDate());
        card.setProductId(document.getProductId());
        card.setCustomer(document.getCustomer());
        card.setTypeCurrency(document.getTypeCurrency());
        card.setCardStatus(document.getCardStatus());
        if (Objects.nonNull(document.getCreditLimit())) {
            card.setCreditLimit(document.getCreditLimit());
            card.setCurrentBalance(document.getCurrentBalance());
            card.setInterestRate(document.getInterestRate());
            card.setUsedBalance(document.getCreditLimit() - document.getCurrentBalance());
            card.setMinimumPayment(card.getUsedBalance() * (document.getInterestRate() * 3));
            card.setPaymentDate(document.getPaymentDate());
        }
        return card;
    }

    public CardDetailResponse dtoTOModelDetail(CardDTO dto) {
        CardDetailResponse card = new CardDetailResponse();
        card.setId(dto.getId());
        card.setCardNumber(dto.getCardNumber());
        card.setCvv(dto.getCvv());
        card.setOpeningDate(dto.getOpeningDate());
        card.setExpirationDate(dto.getExpirationDate());
        card.setProduct(productMapper.documentToModel(dto.getProduct()));
        CardResponseCustomer customer = new CardResponseCustomer();
        customer.setId(dto.getCustomer().getId());
        customer.setType(dto.getCustomer().getType());
        card.setCustomer(customer);
        card.setTypeCurrency(dto.getTypeCurrency());
        card.setCardStatus(dto.getCardStatus());
        card.setCreditLimit(dto.getCreditLimit());
        card.setCurrentBalance(dto.getCurrentBalance());
        card.setInterestRate(dto.getInterestRate());
        card.setUsedBalance(dto.getUsedBalance());
        card.setMinimumPayment(dto.getMinimumPayment());
        card.setPaymentDate(dto.getPaymentDate());
        card.setTransactions(
                dto.getTransactions()
                        .stream()
                        .map(transaction -> transactionMapper.documentToModel(transaction))
                        .toList()
        );
        return card;
    }

    public CardResponse dtoToModel(CardDTO dto) {
        CardResponse card = new CardResponse();
        card.setId(dto.getId());
        card.setCardNumber(dto.getCardNumber());
        card.setCvv(dto.getCvv());
        card.setOpeningDate(dto.getOpeningDate());
        card.setExpirationDate(dto.getExpirationDate());
        card.setProduct(productMapper.documentToModel(dto.getProduct()));
        CardResponseCustomer customer = new CardResponseCustomer();
        customer.setId(dto.getCustomer().getId());
        customer.setType(dto.getCustomer().getType());
        card.setCustomer(customer);
        card.setTypeCurrency(dto.getTypeCurrency());
        card.setCardStatus(dto.getCardStatus());
        card.setCreditLimit(dto.getCreditLimit());
        card.setCurrentBalance(dto.getCurrentBalance());
        card.setInterestRate(dto.getInterestRate());
        card.setUsedBalance(dto.getUsedBalance());
        card.setMinimumPayment(dto.getMinimumPayment());
        card.setPaymentDate(dto.getPaymentDate());
        return card;
    }

    public CardDTO modelToDto(CardRequest model) {
        CardDTO card = new CardDTO();
        card.setCardNumber(model.getCardNumber());
        card.setCvv(model.getCvv());
        card.setOpeningDate(model.getOpeningDate());
        card.setExpirationDate(model.getExpirationDate());
        card.setProductId(model.getProductId());
        Customer customer = new Customer();
        customer.setId(model.getCustomer().getId());
        customer.setType(model.getCustomer().getType());
        card.setCustomer(customer);
        card.setTypeCurrency(getTypeCurrency(model.getTypeCurrency()));
        card.setCardStatus(getCardStatus(model.getCardStatus()));
        card.setCreditLimit(model.getCreditLimit());
        card.setCurrentBalance(model.getCurrentBalance());
        card.setInterestRate(model.getInterestRate());
        card.setPaymentDate(model.getPaymentDate());
        card.setAccountId(model.getAccountId());
        return card;
    }

    public Card dtoToDocument(CardDTO dto) {
        Card card = new Card();
        card.setCardNumber(dto.getCardNumber());
        card.setCvv(dto.getCvv());
        card.setOpeningDate(dto.getOpeningDate());
        card.setExpirationDate(dto.getExpirationDate());
        card.setProductId(dto.getProductId());
        card.setCustomer(dto.getCustomer());
        card.setTypeCurrency(dto.getTypeCurrency());
        card.setCardStatus(dto.getCardStatus());
        card.setCreditLimit(dto.getCreditLimit());
        card.setCurrentBalance(dto.getCurrentBalance());
        card.setInterestRate(dto.getInterestRate());
        card.setPaymentDate(dto.getPaymentDate());
        return card;
    }

    private String getCardStatus(CardRequest.CardStatusEnum cardStatusEnum) {
        return switch (cardStatusEnum) {
            case A -> "Activa";
            case B -> "Bloqueada";
            case C -> "Caducada";
        };
    }

    private String getTypeCurrency(CardRequest.TypeCurrencyEnum typeCurrencyEnum) {
        return switch (typeCurrencyEnum) {
            case PEN -> "Soles";
            case USD -> "Dolares";
        };
    }
}