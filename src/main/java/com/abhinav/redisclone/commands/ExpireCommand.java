package com.abhinav.redisclone.commands;

import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class ExpireCommand implements Command {
    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {
        Database database = Database.getInstance();
        String key = arguments[1];
        long seconds = Long.parseLong(arguments[2]);

        if (!database.exists(key)) {
            outputStream.write(":0\r\n".getBytes());
            outputStream.flush();
            return;
        }
        database.setExpiry(key, seconds);
        outputStream.write(":1\r\n".getBytes());
        outputStream.flush();
    }
}