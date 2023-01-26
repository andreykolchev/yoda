package com.example.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountOperation {
    @JsonProperty(value = "sum")
    private Double sum;
    @JsonProperty(value = "account")
    private Long account;
    @JsonProperty(value = "commissionAccount")
    private Long commissionAccount;
}
