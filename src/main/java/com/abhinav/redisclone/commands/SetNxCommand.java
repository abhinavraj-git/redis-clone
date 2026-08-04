package com.abhinav.redisclone.commands;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class SetNxCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();

        String key = arguments[1];
        String value = arguments[2];
        boolean success = database.setNx(key, value);

        if (success) {
            outputStream.write(":1\r\n".getBytes());
        } else {
            outputStream.write(":0\r\n".getBytes());
        }

        outputStream.flush();
    }
}