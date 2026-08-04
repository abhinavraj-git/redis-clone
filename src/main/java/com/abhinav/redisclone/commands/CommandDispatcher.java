package com.abhinav.redisclone.commands;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, Command> commands = new HashMap<>();
    public CommandDispatcher() {

        commands.put("PING", new PingCommand());
        commands.put("SET", new SetCommand());
        commands.put("GET", new GetCommand());
        commands.put("DEL", new DelCommand());
        commands.put("EXISTS", new ExistsCommand());
        commands.put("INCR", new IncrCommand());
        commands.put("DECR", new DecrCommand());
        commands.put("MSET", new MSetCommand());
        commands.put("MGET", new MGetCommand());
        commands.put("APPEND", new AppendCommand());
        commands.put("STRLEN", new StrLenCommand());
        commands.put("SETNX", new SetNxCommand());
        commands.put("KEYS", new KeysCommand());
        commands.put("TYPE", new TypeCommand());
        commands.put("EXPIRE", new ExpireCommand());
        commands.put("SAVE", new SaveCommand());
    }
    public void dispatch(String[] arguments, OutputStream outputStream) throws IOException {
        Command command = commands.get(arguments[0].toUpperCase());

        if (command != null) {
            command.execute(arguments, outputStream);
        } else {
            outputStream.write("-ERR unknown command\r\n".getBytes());
            outputStream.flush();
        }
    }

}
