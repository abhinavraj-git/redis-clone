package com.abhinav.redisclone.server;
import com.abhinav.redisclone.storage.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisServer {
    private static final int DEFAULT_PORT = 6380;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public void start() {
        try {
            Database.getInstance().loadFromDisk();
            Thread cleaner = new Thread(new ExpiryCleaner());
            cleaner.setDaemon(true);
            cleaner.start();
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("Redis Server listening on port " + DEFAULT_PORT);
            while (true) {
                System.out.println("Waiting for a client...");
                Socket clientSocket = serverSocket.accept();
                ServerStats.getInstance().clientConnected();
                System.out.println("Client connected!");
                threadPool.execute(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}