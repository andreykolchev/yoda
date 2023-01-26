package com.example.application.service;

import com.example.application.config.Cache;
import com.example.application.kafka.ResponseSender;
import com.example.application.model.command.CommandMessage;
import com.example.application.model.command.ResponseMessage;
import com.example.application.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoService {

    final ResponseSender responseSender;

    public UserInfoService(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @SneakyThrows
    public void getUserAccounts(CommandMessage cm) {
        val user = cm.getContext().get("user").asText();
        Thread.sleep(1000);
        val userAccounts = Cache.getAccounts(user);
        val jsonData = JsonUtil.toJsonNode(userAccounts);
        val response = new ResponseMessage(
                cm.getId(),
                cm.getContext(),
                null,
                jsonData);
        responseSender.sendToBpe(JsonUtil.toJson(response));
    }

    @SneakyThrows
    public void getUserInfo(CommandMessage cm) {
        val user = cm.getContext().get("user").asText();
        Thread.sleep(1000);
        val userInfo = Cache.getUserInfo(user);
        val jsonData = JsonUtil.toJsonNode(userInfo);
        val response = new ResponseMessage(
                cm.getId(),
                cm.getContext(),
                null,
                jsonData);
        responseSender.sendToBpe(JsonUtil.toJson(response));
    }
}
