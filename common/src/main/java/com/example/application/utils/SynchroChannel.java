package com.example.application.utils;

import com.example.application.model.command.ResponseMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class SynchroChannel {
    private final ConcurrentHashMap<String, ArrayBlockingQueue<ResponseMessage>> syncChannel = new ConcurrentHashMap<>();

    private ArrayBlockingQueue<ResponseMessage> getQueue(String key) {
        if (syncChannel.get(key) == null) {
            syncChannel.put(key, new ArrayBlockingQueue<>(1));
        }
        return syncChannel.get(key);
    }

    public void putMessage(String key, ResponseMessage data) {
        try {
            getQueue(key).put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ResponseMessage getMessage(String key) {
        ResponseMessage data = null;
        try {
            data = getQueue(key).poll(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        syncChannel.remove(key);
        return data;
    }

    public Boolean containsKey(String key) {
        return syncChannel.containsKey(key);
    }
}
