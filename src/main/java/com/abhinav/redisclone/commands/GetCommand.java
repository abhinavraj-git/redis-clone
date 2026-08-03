package com.abhinav.redisclone.commands;

import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class GetCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();

        String key = arguments[1];
        String value = database.get(key);

        if (value == null) {
            outputStream.write("$-1\r\n".getBytes());
        } else {
            outputStream.write(("$" + value.length() + "\r\n").getBytes());
            outputStream.write((value + "\r\n").getBytes());
        }

        outputStream.flush();
    }
}
