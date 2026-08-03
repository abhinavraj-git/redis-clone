package com.abhinav.redisclone.server;
import com.abhinav.redisclone.commands.CommandDispatcher;
import com.abhinav.redisclone.protocol.RespParser;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}