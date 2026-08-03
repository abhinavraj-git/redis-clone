package com.abhinav.redisclone.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.abhinav.redisclone.protocol.RespParser;

import com.abhinav.redisclone.commands.CommandDispatcher;

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
                InputStream inputStream = clientSocket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                RespParser parser = new RespParser(reader);
                OutputStream outputStream = clientSocket.getOutputStream();
                CommandDispatcher commandDispatcher = new CommandDispatcher();
                while (true) {
                    String[] arguments = parser.parse();
                    if (arguments == null) {
                        break;
                    }
                    System.out.println("Received:");
                    for (String argument : arguments) {
                        System.out.println(argument);
                    }
                    commandDispatcher.dispatch(arguments, outputStream);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}