package com.example.application.service;

import com.example.application.model.Account;
import com.example.application.model.UserAccounts;
import com.example.application.model.UserInfo;
import com.example.application.model.command.CommandMessage;
import com.example.application.model.command.ResponseMessage;
import com.example.application.utils.JsonUtil;
import com.example.application.utils.SynchroChannel;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OperationService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SynchroChannel channel;

    public OperationService(KafkaTemplate<String, String> kafkaTemplate, SynchroChannel channel) {
        this.kafkaTemplate = kafkaTemplate;
        this.channel = channel;
    }

    public List<Account> getAccounts(String operationId, String user) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        val context = JsonUtil.toJsonNode(params);
        val data = JsonUtil.empty();
        CommandMessage commandMessage = new CommandMessage(
                operationId,
                "getAccounts",
                context,
                data
        );
        String message = JsonUtil.toJson(commandMessage);
        sendToBpe(message);
        val responseMessage = channel.getMessage(operationId);
        if (responseMessage != null) {
            if (responseMessage.getError() == null) {
                val result = JsonUtil.toObject(UserAccounts.class, responseMessage.getData());
                return result.getAccounts();
            } else {
                throw new RuntimeException(responseMessage.getError().getMessage());
            }
        } else {
            throw new RuntimeException("No response from bpe service");
        }
    }

    public UserInfo getUserInfo(String operationId, String user) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        val context = JsonUtil.toJsonNode(params);
        val data = JsonUtil.empty();
        CommandMessage commandMessage = new CommandMessage(
                operationId,
                "getUserInfo",
                context,
                data
        );
        String message = JsonUtil.toJson(commandMessage);
        sendToBpe(message);
        val responseMessage = channel.getMessage(operationId);
        if (responseMessage != null) {
            if (responseMessage.getError() == null) {
                return JsonUtil.toObject(UserInfo.class, responseMessage.getData());
            } else {
                throw new RuntimeException(responseMessage.getError().getMessage());
            }
        } else {
            throw new RuntimeException("No response from bpe service");
        }
    }

    private void sendToBpe(String msg) {
        kafkaTemplate.send("bpeRq", msg);
    }

    @KafkaListener(topics = "bpeRs", groupId = "bpe")
    public void receiveFromBpe(
            String message,
            @Header(KafkaHeaders.ACKNOWLEDGMENT) final Acknowledgment ack
    ) {
        val responseMessage = JsonUtil.toObject(ResponseMessage.class, message);
        if (channel.containsKey(responseMessage.getId())) {
            channel.putMessage(responseMessage.getId(), responseMessage);
            ack.acknowledge();
            log.info("Received Message in group bpe: " + message);
        }
    }
}
