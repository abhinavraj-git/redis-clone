package com.abhinav.redisclone.commands;
import java.io.IOException;
import java.io.OutputStream;

public class PingCommand implements Command {

    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        outputStream.write("+PONG\r\n".getBytes());
        outputStream.flush();

    }

}