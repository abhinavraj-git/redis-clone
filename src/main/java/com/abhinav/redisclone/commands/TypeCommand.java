package com.abhinav.redisclone.commands;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;
public class TypeCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();

        String key = arguments[1];

        if (database.exists(key)) {
            outputStream.write("+string\r\n".getBytes());
        } else {
            outputStream.write("+none\r\n".getBytes());
        }

        outputStream.flush();
    }
}