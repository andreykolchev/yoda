package com.example.application.delegate.payment;

import com.example.application.model.command.CommandMessage;
import com.example.application.model.command.ResponseMessage;
import com.example.application.utils.JsonUtil;
import com.example.application.utils.SynchroChannel;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentGetAccountBalance implements JavaDelegate {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SynchroChannel channel;

    public PaymentGetAccountBalance(KafkaTemplate<String, String> kafkaTemplate, SynchroChannel channel) {
        this.kafkaTemplate = kafkaTemplate;
        this.channel = channel;
    }

    @Override
    public void execute(final DelegateExecution execution) {
        log.info(execution.getCurrentActivityId());
        String data = (String) execution.getVariable("data");
        CommandMessage executionData = JsonUtil.toObject(CommandMessage.class, data);
        val operationId = executionData.getId();
        CommandMessage commandMessage = new CommandMessage(
                operationId,
                "getAccountBalance",
                executionData.getContext(),
                executionData.getData()
        );
        kafkaTemplate.send("paymentRq", JsonUtil.toJson(commandMessage));
        val responseMessage = channel.getMessage(operationId);
        executionData.setData(responseMessage.getData());
        execution.setVariable("data", JsonUtil.toJson(executionData));
    }

    @KafkaListener(topics = "paymentRs", groupId = "payment")
    public void receive(
            String message,
            @Header(KafkaHeaders.ACKNOWLEDGMENT) final Acknowledgment ack
    ) {
        val responseMessage = JsonUtil.toObject(ResponseMessage.class, message);
        if (channel.containsKey(responseMessage.getId())) {
            channel.putMessage(responseMessage.getId(), responseMessage);
            ack.acknowledge();
        }
    }
}

