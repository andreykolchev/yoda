package com.example.application.model.command;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    String id;
    JsonNode context;
    ResponseErrorMessage error;
    JsonNode data;
}

