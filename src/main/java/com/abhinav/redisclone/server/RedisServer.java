package com.abhinav.redisclone.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RedisServer {
    private static final int DEFAULT_PORT = 6380;
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("Redis Server listening on port " + DEFAULT_PORT);
            while (true) {
                System.out.println("Waiting for a client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}