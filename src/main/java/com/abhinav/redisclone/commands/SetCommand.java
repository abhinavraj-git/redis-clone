package com.abhinav.redisclone.commands;

import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class SetCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();

        String key = arguments[1];
        String value = arguments[2];

        database.set(key, value);

        outputStream.write("+OK\r\n".getBytes());
        outputStream.flush();
    }
}