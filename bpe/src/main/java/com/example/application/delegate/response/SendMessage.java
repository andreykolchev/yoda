package com.example.application.delegate.response;

import com.example.application.kafka.ResponseSender;
import com.example.application.model.command.ResponseMessage;
import com.example.application.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMessage implements JavaDelegate {

    private final ResponseSender responseSender;

    public SendMessage(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @Override
    public void execute(final DelegateExecution execution) {
        log.info(execution.getCurrentActivityId());
        String data = (String) execution.getVariable("data");
        ResponseMessage responseMessage = JsonUtil.toObject(ResponseMessage.class, data);
        responseSender.sendToGateway(JsonUtil.toJson(responseMessage));
    }
}

