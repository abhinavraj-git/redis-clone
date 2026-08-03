package com.abhinav.redisclone.commands;

import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class AppendCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();

        String key = arguments[1];
        String appendValue = arguments[2];

        String currentValue = database.get(key);

        if (currentValue == null) {
            currentValue = "";
        }

        String newValue = currentValue + appendValue;

        database.set(key, newValue);

        outputStream.write((":" + newValue.length() + "\r\n").getBytes());
        outputStream.flush();
    }
}