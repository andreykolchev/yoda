package com.example.application.config;

import com.example.application.model.UserAccounts;
import com.example.application.model.UserInfo;

import java.util.Map;

public class Cache {
    private static Map<String, UserAccounts> accounts;
    private static Map<String, UserInfo> userInfo;

    public static void setInitData(
            final Map<String, UserAccounts> accountsMap,
            final Map<String, UserInfo> userInfoMap
    ) {
        accounts = accountsMap;
        userInfo = userInfoMap;
    }

    public static UserAccounts getAccounts(final String user) {
        return accounts.get(user);
    }

    public static UserInfo getUserInfo(String user) {
        return userInfo.get(user);
    }
}
