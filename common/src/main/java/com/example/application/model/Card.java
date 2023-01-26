package com.example.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
    @JsonProperty(value = "cardNumber")
    private String cardNumber;
    @JsonProperty(value = "expires")
    private String expires;
    @JsonProperty(value = "cardType")
    private Integer cardType;
}
