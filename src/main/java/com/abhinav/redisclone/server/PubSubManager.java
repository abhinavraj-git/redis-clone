package com.abhinav.redisclone.server;

import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PubSubManager {

    private static final PubSubManager INSTANCE = new PubSubManager();

    public static PubSubManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, Set<OutputStream>> channels =
            new ConcurrentHashMap<>();

    public synchronized void subscribe(String channel, OutputStream client) {
        channels.computeIfAbsent(channel, key -> ConcurrentHashMap.newKeySet());
        channels.get(channel).add(client);
    }

    public synchronized void unsubscribe(String channel, OutputStream client) {
        Set<OutputStream> subscribers = channels.get(channel);
        if (subscribers == null) {
            return;
        }
        subscribers.remove(client);
        if (subscribers.isEmpty()) {
            channels.remove(channel);
        }
    }

    public synchronized int publish(String channel, String message) {
        Set<OutputStream> subscribers = channels.get(channel);
        if (subscribers == null) {
            return 0;
        }
        int delivered = 0;
        for (OutputStream client : subscribers) {
            try {
                String resp =
                        "*3\r\n" +
                                "$7\r\nmessage\r\n" +
                                "$" + channel.length() + "\r\n" +
                                channel + "\r\n" +
                                "$" + message.length() + "\r\n" +
                                message + "\r\n";

                client.write(resp.getBytes());
                client.flush();
                delivered++;
            } catch (Exception e) {
            }
        }

        return delivered;
    }
}