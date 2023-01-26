package com.example.application.model.command;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommandMessage {
    private String id;
    private String command;
    private JsonNode context;
    private JsonNode data;
}

