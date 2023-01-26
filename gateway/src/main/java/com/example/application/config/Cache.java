package com.example.application.config;

import com.example.application.model.UserAccounts;
import com.example.application.model.UserInfo;

import java.util.Map;

public class Cache {
    private static Map<String, UserAccounts> accounts;
    private static Map<String, UserInfo> userInfo;
    private static Map<String, Double> balance;

    public static void setInitData(
            final Map<String, UserAccounts> accountsMap,
            final Map<String, UserInfo> userInfoMap,
            final Map<String, Double> balanceMap
    ) {
        accounts = accountsMap;
        userInfo = userInfoMap;
        balance = balanceMap;
    }

    public static UserAccounts getAccounts(final String user) {
        return accounts.get(user);
    }

    public static void fillBalances(final UserAccounts userAccounts) {
        userAccounts.getAccounts().forEach(account -> account.setBalance(getBalance(account.getAccountNumber())));
    }

    public static UserInfo getUserInfo(String user) {
        return userInfo.get(user);
    }

    public static Double getBalance(Long account) {
        return balance.get(account.toString());
    }

    public static void chargeBalance(Long account, Double sum) {
        Double currentBalance = balance.get(account.toString());
        balance.put(account.toString(), currentBalance + sum);
    }

    public static void withdrawBalance(Long account, Double sum) {
        Double currentBalance = balance.get(account.toString());
        if (sum <= currentBalance) {
            balance.put(account.toString(), currentBalance - sum);
        } else {
            throw new RuntimeException("Not enough money!");
        }
    }
}
