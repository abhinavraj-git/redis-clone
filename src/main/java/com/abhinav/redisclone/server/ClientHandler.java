package com.abhinav.redisclone.server;
import com.abhinav.redisclone.commands.CommandDispatcher;
import com.abhinav.redisclone.protocol.RespParser;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private boolean inTransaction = false;
    private final List<String[]> queuedCommands = new ArrayList<>();
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

                String command = arguments[0].toUpperCase();
                if (command.equals("MULTI")) {
                    if (inTransaction) {
                        outputStream.write("-ERR MULTI calls can not be nested\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    inTransaction = true;
                    queuedCommands.clear();

                    outputStream.write("+OK\r\n".getBytes());
                    outputStream.flush();

                } else if (command.equals("EXEC")) {
                    if (!inTransaction) {
                        outputStream.write("-ERR EXEC without MULTI\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    inTransaction = false;
                    for (String[] queuedCommand : queuedCommands) {
                        commandDispatcher.dispatch(queuedCommand, outputStream);
                    }
                    queuedCommands.clear();

                }else if (command.equals("DISCARD")) {
                    if (!inTransaction) {
                        outputStream.write("-ERR DISCARD without MULTI\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    queuedCommands.clear();
                    inTransaction = false;
                    outputStream.write("+OK\r\n".getBytes());
                    outputStream.flush();

                }else if (inTransaction) {
                    queuedCommands.add(arguments);
                    outputStream.write("+QUEUED\r\n".getBytes());
                    outputStream.flush();
                } else {
                    commandDispatcher.dispatch(arguments, outputStream);
                }
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