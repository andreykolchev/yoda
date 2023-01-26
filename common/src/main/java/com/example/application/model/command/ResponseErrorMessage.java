package com.example.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseErrorMessage {
    String code;
    String message;
}
