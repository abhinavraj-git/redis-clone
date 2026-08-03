package com.abhinav.redisclone.commands;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
public class KeysCommand implements Command {

    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {

        Database database = Database.getInstance();

        Set<String> keys = database.keys();

        outputStream.write(("*" + keys.size() + "\r\n").getBytes());

        for (String key : keys) {
            outputStream.write(("$" + key.length() + "\r\n").getBytes());
            outputStream.write((key + "\r\n").getBytes());
        }

        outputStream.flush();
    }
}