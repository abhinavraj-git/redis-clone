package com.abhinav.redisclone.server;

public class AuthManager {

    private static final AuthManager INSTANCE = new AuthManager();
    private static final String PASSWORD = "redis123";
    private AuthManager() {
    }

    public static AuthManager getInstance() {
        return INSTANCE;
    }

    public boolean authenticate(String password) {
        return PASSWORD.equals(password);
    }
}