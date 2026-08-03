package com.abhinav.redisclone.commands;

import java.io.IOException;
import java.io.OutputStream;

public interface Command {

    void execute(String[] arguments, OutputStream outputStream) throws IOException;

}