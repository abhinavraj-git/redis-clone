package com.abhinav.redisclone.protocol;
import java.io.BufferedReader;
import java.io.IOException;

public class RespParser {
    private final BufferedReader reader;

    public RespParser(BufferedReader reader) {
        this.reader = reader;
    }

    public String[] parse() throws IOException {
        int firstByte = reader.read();
        if (firstByte == -1) {
            return null;
        }
        if (firstByte != '*') {
            throw new IOException("Invalid RESP Array");
        }
        int arrayLength = Integer.parseInt(reader.readLine());
        String[] arguments = new String[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            int bulkString = reader.read();
            if (bulkString != '$') {
                throw new IOException("Expected Bulk String");
            }
            String bulkLength = reader.readLine();
            int stringLength = Integer.parseInt(bulkLength);
            String value = reader.readLine();
            arguments[i] = value;
        }
        return arguments;
    }
}