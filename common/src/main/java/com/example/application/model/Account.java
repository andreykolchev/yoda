package com.example.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Account {
    @JsonProperty(value = "accountNumber")
    private Long accountNumber;
    @JsonProperty(value = "balance")
    private Double balance;
    @JsonProperty(value = "cards")
    private List<Card> cards;
}
