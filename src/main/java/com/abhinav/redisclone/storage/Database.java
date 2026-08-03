package com.abhinav.redisclone.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {
    private static final Database INSTANCE = new Database();
    public static Database getInstance() {
        return INSTANCE;
    }
    private final Map<String, String> data = new HashMap<>();
    public void set(String key, String value) {
        data.put(key, value);
    }
    public String get(String key) {
        return data.get(key);
    }
    public boolean delete(String key) {
        return data.remove(key) != null;
    }
    public boolean exists(String key) {
        return data.containsKey(key);
    }
    public Set<String> keys() {
        return data.keySet();
    }
    public void clear() {
        data.clear();
    }

}