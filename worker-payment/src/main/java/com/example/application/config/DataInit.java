package com.example.application.config;

import com.example.application.utils.JsonUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataInit implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) {
        System.out.println("###############################Init Data###############################");
        Cache.setInitData(
                getBalance()
        );
    }

    private Map<String, Double> getBalance() {
        String json = JsonUtil.readFile("data/account-balance.json");
        return JsonUtil.toMap(HashMap.class, String.class, Double.class, json);
    }

}
