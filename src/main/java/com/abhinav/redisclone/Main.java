package com.abhinav.redisclone;
import com.abhinav.redisclone.server.RedisServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Redis Server...");
        RedisServer server = new RedisServer();
        server.start();
    }

}