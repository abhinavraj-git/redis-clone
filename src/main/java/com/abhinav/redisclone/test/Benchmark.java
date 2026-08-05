package com.abhinav.redisclone.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Benchmark {

    private static final int TOTAL_COMMANDS = 100000;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket("localhost", 6380);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()))
        ) {

            // Authenticate
            send(writer, "AUTH", "redis123");
            System.out.println(reader.readLine());

            long start = System.nanoTime();

            for (int i = 0; i < TOTAL_COMMANDS; i++) {

                send(writer,
                        "SET",
                        "key" + i,
                        "value" + i);

                reader.readLine();
            }

            long end = System.nanoTime();

            double seconds = (end - start) / 1_000_000_000.0;

            System.out.println("--------------------------------");
            System.out.println("Commands : " + TOTAL_COMMANDS);
            System.out.printf("Time     : %.3f sec%n", seconds);
            System.out.printf("Throughput: %.0f ops/sec%n",
                    TOTAL_COMMANDS / seconds);
            System.out.println("--------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void send(BufferedWriter writer, String... args)
            throws Exception {

        writer.write("*" + args.length + "\r\n");

        for (String arg : args) {
            writer.write("$" + arg.length() + "\r\n");
            writer.write(arg + "\r\n");
        }

        writer.flush();
    }
}