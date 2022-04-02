package com.github.simple.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GreetingServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("hello from gRPC");

    // Step 1: Create a Server on a port
    // Step 2: Build the Server
    // Step 3: Start the server
    // Step 4: await the server to close on block on main termination
    Server server = ServerBuilder.forPort(50051)
            .addService(new GreetServiceImpl())
            .build();

    server.start();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  System.out.println("Received Shutdown request");
                  server.shutdown();
                  System.out.println("Shutdown successfully stopped the server");
                }));

        server.awaitTermination();
    }
}

