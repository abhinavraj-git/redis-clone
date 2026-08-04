package com.abhinav.redisclone.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class Database {
    private static final Database INSTANCE = new Database();
    public static Database getInstance() {
        return INSTANCE;
    }
    private final Map<String, String> data = new ConcurrentHashMap<>();
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
    public synchronized int increment(String key) {
        String value = data.getOrDefault(key, "0");
        int number = Integer.parseInt(value);
        number++;
        data.put(key, String.valueOf(number));
        return number;
    }

    public synchronized int decrement(String key) {
        String value = data.getOrDefault(key, "0");
        int number = Integer.parseInt(value);
        number--;
        data.put(key, String.valueOf(number));
        return number;
    }

    public synchronized int append(String key, String suffix) {
        String current = data.getOrDefault(key, "");
        current = current + suffix;
        data.put(key, current);
        return current.length();
    }

    public synchronized boolean setNx(String key, String value) {
        if (data.containsKey(key)) {
            return false;
        }
        data.put(key, value);
        return true;
    }

    public synchronized void mset(String[] arguments) {
        for (int i = 1; i < arguments.length; i += 2) {
            data.put(arguments[i], arguments[i + 1]);
        }

    }

    public synchronized String[] mget(String[] arguments) {
        String[] values = new String[arguments.length - 1];
        for (int i = 1; i < arguments.length; i++) {
            values[i - 1] = data.get(arguments[i]);
        }
        return values;
    }
}