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
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream));

            RespParser parser = new RespParser(reader);
            OutputStream outputStream = clientSocket.getOutputStream();
            CommandDispatcher commandDispatcher = new CommandDispatcher();
            while (true) {
                String[] arguments = parser.parse();
                if (arguments == null) {
                    break;
                }
                ServerStats.getInstance().commandProcessed();
                System.out.println("Received:");
                for (String argument : arguments) {
                    System.out.println(argument);
                }
                String command = arguments[0].toUpperCase();

                if (command.equals("SUBSCRIBE")) {

                    if (arguments.length != 2) {
                        outputStream.write("-ERR wrong number of arguments\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    PubSubManager.getInstance()
                            .subscribe(arguments[1], outputStream);
                    String response =
                            "*3\r\n" +
                                    "$9\r\nsubscribe\r\n" +
                                    "$" + arguments[1].length() + "\r\n" +
                                    arguments[1] + "\r\n" +
                                    ":1\r\n";
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    continue;
                }

                if (command.equals("UNSUBSCRIBE")) {
                    if (arguments.length != 2) {
                        outputStream.write("-ERR wrong number of arguments\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    PubSubManager.getInstance()
                            .unsubscribe(arguments[1], outputStream);
                    String response =
                            "*3\r\n" +
                                    "$11\r\nunsubscribe\r\n" +
                                    "$" + arguments[1].length() + "\r\n" +
                                    arguments[1] + "\r\n" +
                                    ":0\r\n";
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    continue;
                }

                if (command.equals("PUBLISH")) {
                    if (arguments.length != 3) {
                        outputStream.write("-ERR wrong number of arguments\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    int delivered = PubSubManager.getInstance()
                            .publish(arguments[1], arguments[2]);
                    outputStream.write((":" + delivered + "\r\n").getBytes());
                    outputStream.flush();
                    continue;
                }

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
                    continue;
                }

                if (command.equals("EXEC")) {
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
                    continue;
                }

                if (command.equals("DISCARD")) {
                    if (!inTransaction) {
                        outputStream.write("-ERR DISCARD without MULTI\r\n".getBytes());
                        outputStream.flush();
                        continue;
                    }
                    queuedCommands.clear();
                    inTransaction = false;
                    outputStream.write("+OK\r\n".getBytes());
                    outputStream.flush();
                    continue;
                }

                if (inTransaction) {
                    queuedCommands.add(arguments);
                    outputStream.write("+QUEUED\r\n".getBytes());
                    outputStream.flush();
                    continue;
                }
                commandDispatcher.dispatch(arguments, outputStream);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                ServerStats.getInstance().clientDisconnected();
                clientSocket.close();
            } catch (IOException ignored) {
            }

        }

    }
}