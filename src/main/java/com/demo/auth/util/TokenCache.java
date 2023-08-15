package com.demo.auth.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenCache {

    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public static String getToken(String username) {
        return tokenMap.get(username);
    }

    public static void putToken(String username, String token) {
        tokenMap.put(username, token);
    }

    public static void removeToken(String username) {
        tokenMap.remove(username);
    }

}
