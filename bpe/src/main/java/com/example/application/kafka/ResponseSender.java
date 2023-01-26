package com.example.application.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ResponseSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToGateway(String msg) {
        kafkaTemplate.send("bpeRs", msg);
    }
}
