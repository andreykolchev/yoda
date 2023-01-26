package com.example.application.config;

import com.example.application.model.UserAccounts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private static ConcurrentHashMap<String, Double> balance;

    public static void setInitData(
            final Map<String, Double> balanceMap
    ) {
        balance = new ConcurrentHashMap<>(balanceMap);
    }

    public static void fillBalances(final UserAccounts userAccounts) {
        userAccounts.getAccounts().forEach(account -> account.setBalance(getBalance(account.getAccountNumber())));
    }

    public static Double getBalance(Long account) {
        return balance.get(account.toString());
    }

    public static void chargeBalance(Long account, Double sum) {
        Double currentBalance = balance.get(account.toString());
        balance.put(account.toString(), currentBalance + sum);
    }

    public static void withdrawBalance(Long account, Double sum) throws RuntimeException {
        Double currentBalance = balance.get(account.toString());
        if (sum <= currentBalance) {
            balance.put(account.toString(), currentBalance - sum);
        } else {
            throw new RuntimeException("Not enough money!");
        }
    }
}
