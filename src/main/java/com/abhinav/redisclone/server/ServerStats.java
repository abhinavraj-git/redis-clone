package com.abhinav.redisclone.server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServerStats {

    private static final ServerStats INSTANCE = new ServerStats();
    private final long startTime = System.currentTimeMillis();
    private final AtomicInteger connectedClients = new AtomicInteger(0);
    private final AtomicLong totalCommandsProcessed = new AtomicLong(0);

    private ServerStats() {
    }

    public static ServerStats getInstance() {
        return INSTANCE;
    }

    public void clientConnected() {
        connectedClients.incrementAndGet();
    }

    public void clientDisconnected() {
        connectedClients.decrementAndGet();
    }

    public void commandProcessed() {
        totalCommandsProcessed.incrementAndGet();
    }

    public int getConnectedClients() {
        return connectedClients.get();
    }

    public long getTotalCommandsProcessed() {
        return totalCommandsProcessed.get();
    }

    public long getUptimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}