package com.abhinav.redisclone.test;
import com.abhinav.redisclone.storage.Database;

public class ConcurrencyTest {
    public static void main(String[] args) throws InterruptedException {
        Database database = Database.getInstance();
        database.set("counter", "0");
        int threads = 1000;
        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                database.increment("counter");
            });
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        System.out.println("Expected : " + threads);
        System.out.println("Actual   : " + database.get("counter"));
    }
}