package com.abhinav.redisclone.commands;

import com.abhinav.redisclone.server.ServerStats;
import com.abhinav.redisclone.storage.Database;
import java.io.IOException;
import java.io.OutputStream;

public class InfoCommand implements Command {
    @Override
    public void execute(String[] arguments, OutputStream outputStream) throws IOException {
        Database database = Database.getInstance();
        ServerStats stats = ServerStats.getInstance();

        String info =
                "redis_clone_version:1.0\r\n" +
                        "total_keys:" + database.getTotalKeys() + "\r\n" +
                        "expiring_keys:" + database.getExpiringKeys() + "\r\n" +
                        "connected_clients:" + stats.getConnectedClients() + "\r\n" +
                        "uptime_seconds:" + stats.getUptimeSeconds() + "\r\n" +
                        "total_commands_processed:" + stats.getTotalCommandsProcessed() + "\r\n";

        outputStream.write(("$" + info.length() + "\r\n").getBytes());
        outputStream.write((info + "\r\n").getBytes());
        outputStream.flush();
    }
}