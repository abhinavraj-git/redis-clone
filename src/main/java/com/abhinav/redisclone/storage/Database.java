package com.abhinav.redisclone.storage;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Database implements Serializable {

    private static final Database INSTANCE = new Database();
    private static final String SAVE_FILE = "dump.rdb";

    public static Database getInstance() {
        return INSTANCE;
    }

    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final Map<String, Long> expiry = new ConcurrentHashMap<>();

    public synchronized void set(String key, String value) {
        data.put(key, value);
    }

    public synchronized String get(String key) {
        if (isExpired(key)) {
            return null;
        }
        return data.get(key);
    }

    public synchronized boolean delete(String key) {
        removeExpiry(key);
        return data.remove(key) != null;
    }

    public synchronized boolean exists(String key) {
        if (isExpired(key)) {
            return false;
        }
        return data.containsKey(key);
    }

    public synchronized Set<String> keys() {
        Set<String> keys = new HashSet<>(data.keySet());
        keys.removeIf(this::isExpired);
        return keys;
    }

    public synchronized void clear() {
        data.clear();
        expiry.clear();
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
            values[i - 1] = get(arguments[i]);
        }

        return values;
    }

    public synchronized void setExpiry(String key, long seconds) {
        long expiryTime = System.currentTimeMillis() + (seconds * 1000);
        expiry.put(key, expiryTime);
    }

    public synchronized void removeExpiry(String key) {
        expiry.remove(key);
    }

    public synchronized boolean isExpired(String key) {
        Long expiryTime = expiry.get(key);
        if (expiryTime == null) {
            return false;
        }
        if (System.currentTimeMillis() >= expiryTime) {
            data.remove(key);
            expiry.remove(key);
            return true;
        }
        return false;
    }

    public synchronized void saveToDisk() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(data);
            out.writeObject(expiry);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void loadFromDisk() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Map<String, String> loadedData =
                    (Map<String, String>) in.readObject();

            Map<String, Long> loadedExpiry =
                    (Map<String, Long>) in.readObject();
            data.clear();
            data.putAll(loadedData);
            expiry.clear();
            expiry.putAll(loadedExpiry);

        } catch (IOException | ClassNotFoundException e) {
        }
    }

    public synchronized void cleanupExpiredKeys() {
        for (String key : new HashSet<>(expiry.keySet())) {
            isExpired(key);
        }

    }

    public synchronized int getTotalKeys() {
        return data.size();
    }

    public synchronized int getExpiringKeys() {
        return expiry.size();
    }

}