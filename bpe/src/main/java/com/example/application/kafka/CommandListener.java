package com.example.application.kafka;

import com.example.application.model.command.CommandMessage;
import com.example.application.service.ProcessService;
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

    private final ProcessService processService;

    public CommandListener(ProcessService processService) {
        this.processService = processService;
    }

    @KafkaListener(topics = "bpeRq", groupId = "bpe")
    public void receiveFromGateway(
            String message,
            @Header(KafkaHeaders.ACKNOWLEDGMENT) final Acknowledgment ack
    ) {
        val cm = JsonUtil.toObject(CommandMessage.class, message);
        processService.startProcess(cm);
        ack.acknowledge();
    }
}
