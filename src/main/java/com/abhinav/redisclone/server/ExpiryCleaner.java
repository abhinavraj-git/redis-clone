package com.abhinav.redisclone.server;
import com.abhinav.redisclone.storage.Database;

public class ExpiryCleaner implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Database.getInstance().cleanupExpiredKeys();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}