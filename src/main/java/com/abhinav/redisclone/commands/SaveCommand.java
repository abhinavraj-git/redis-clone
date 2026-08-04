package com.abhinav.redisclone.commands;

import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class SaveCommand implements Command {
    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();
        database.saveToDisk();

        outputStream.write("+OK\r\n".getBytes());
        outputStream.flush();
    }
}