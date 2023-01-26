package com.example.application.service;

import com.example.application.model.command.CommandMessage;
import com.example.application.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProcessService {
    private final RuntimeService runtimeService;

    public ProcessService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void startProcess(final CommandMessage cm) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", JsonUtil.toJson(cm));
        runtimeService.startProcessInstanceByKey(cm.getCommand(), cm.getId(), variables);
    }
}
