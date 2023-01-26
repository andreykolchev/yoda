package com.example.application.service;

import com.example.application.config.Cache;
import com.example.application.kafka.ResponseSender;
import com.example.application.model.AccountOperation;
import com.example.application.model.UserAccounts;
import com.example.application.model.command.CommandMessage;
import com.example.application.model.command.ResponseErrorMessage;
import com.example.application.model.command.ResponseMessage;
import com.example.application.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    final ResponseSender responseSender;

    public PaymentService(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @SneakyThrows
    public void getBalance(CommandMessage cm) {
        UserAccounts userAccounts = JsonUtil.toObject(UserAccounts.class, cm.getData());
        Thread.sleep(1000);
        Cache.fillBalances(userAccounts);
        val jsonData = JsonUtil.toJsonNode(userAccounts);
        val response = new ResponseMessage(
                cm.getId(),
                cm.getContext(),
                null,
                jsonData);
        responseSender.sendToBpe(JsonUtil.toJson(response));
    }

    @SneakyThrows
    public void chargeBalance(CommandMessage cm) {
        AccountOperation operation = JsonUtil.toObject(AccountOperation.class, cm.getData());
        Thread.sleep(1000);
        Cache.chargeBalance(operation.getAccount(), operation.getSum());
        val response = new ResponseMessage(
                cm.getId(),
                cm.getContext(),
                null,
                null);
        responseSender.sendToBpe(JsonUtil.toJson(response));
    }

    @SneakyThrows
    public void withdrawBalance(CommandMessage cm) {
        AccountOperation operation = JsonUtil.toObject(AccountOperation.class, cm.getData());
        Thread.sleep(1000);
        ResponseErrorMessage errorMessage = null;
        try {
            Cache.withdrawBalance(operation.getAccount(), operation.getSum());
        } catch (RuntimeException e) {
            errorMessage = new ResponseErrorMessage("00.01", e.getMessage());
        }
        val response = new ResponseMessage(
                cm.getId(),
                cm.getContext(),
                errorMessage,
                null);
        responseSender.sendToBpe(JsonUtil.toJson(response));
    }
}
