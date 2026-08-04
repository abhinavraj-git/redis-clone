package com.abhinav.redisclone.commands;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class MSetCommand implements Command {

    @Override
    public void execute(String[] arguments,
                        OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();
        if ((arguments.length - 1) % 2 != 0) {

            outputStream.write("-ERR wrong number of arguments for 'mset' command\r\n".getBytes());
            outputStream.flush();

            return;
        }
        database.mset(arguments);
        outputStream.write("+OK\r\n".getBytes());
        outputStream.flush();
    }
}