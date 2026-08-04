package com.abhinav.redisclone.commands;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class MGetCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {
        Database database = Database.getInstance();
        outputStream.write(("*" + (arguments.length - 1) + "\r\n").getBytes());
        String[] values = database.mget(arguments);

        for (String value : values) {
            if (value == null) {
                outputStream.write("$-1\r\n".getBytes());
            } else {
                outputStream.write(("$" + value.length() + "\r\n").getBytes());
                outputStream.write((value + "\r\n").getBytes());
            }
        }
        outputStream.flush();
    }
}