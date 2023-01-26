package com.example.application.kafka;

import com.example.application.model.command.CommandMessage;
import com.example.application.service.PaymentService;
import com.example.application.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandListener {

    private final PaymentService service;

    public CommandListener(PaymentService service) {
        this.service = service;
    }

    @KafkaListener(topics = "paymentRq", groupId = "payment")
    public void receiveFromBpe(
            String message,
            @Header(KafkaHeaders.ACKNOWLEDGMENT) final Acknowledgment ack
    ) {
        val cm = JsonUtil.toObject(CommandMessage.class, message);
        switch (cm.getCommand()) {
            case "charge":
                service.chargeBalance(cm);
                break;
            case "withdraw":
                service.withdrawBalance(cm);
                break;
            case "getAccountBalance":
                service.getBalance(cm);
                break;
        }
        ack.acknowledge();
    }
}
