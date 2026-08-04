package com.abhinav.redisclone.commands;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class DecrCommand implements Command {
    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {
        Database database = Database.getInstance();
        String key = arguments[1];
        String value = database.get(key);
        if (value == null) {
            database.set(key, "-1");
            outputStream.write(":-1\r\n".getBytes());
            outputStream.flush();
            return;
        }
        try {
            int number = database.decrement(key);
            outputStream.write((":" + number + "\r\n").getBytes());
        }
        catch (NumberFormatException e) {
            outputStream.write("-ERR value is not an integer or out of range\r\n".getBytes());
        }
        outputStream.flush();
    }
}