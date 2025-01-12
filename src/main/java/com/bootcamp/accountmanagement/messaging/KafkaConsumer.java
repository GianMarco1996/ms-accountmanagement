package com.bootcamp.accountmanagement.messaging;

import com.bootcamp.accountmanagement.service.transaction.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class KafkaConsumer {

    @Autowired
    private TransactionService transactionService;

    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "monederos-transacciones", groupId = "bootcamp")
    public Mono<String> consumer(String message) throws JsonProcessingException {
        return Mono.just(message)
                .flatMap(m -> transactionService.registerMessage(getMapper(m)));
    }

    private List<KafkaTransaction> getMapper(String message) {
        try {
            return objectMapper.readValue(message,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, KafkaTransaction.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
