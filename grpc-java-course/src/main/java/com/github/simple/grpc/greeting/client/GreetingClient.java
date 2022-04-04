package com.github.simple.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

  public static void main(String[] args) {
    System.out.println("Hello I am a grpc client");
      // usePlaintext disables ssl
      ManagedChannel channel = ManagedChannelBuilder.forAddress(
              "localhost",50051
      ).usePlaintext()
              .build();
    System.out.println("Creating Stub");

    //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
    // Unary
//
//    // created a protocol buffer greeting message
//    Greeting greeting = Greeting.newBuilder()
//                    .setFirstName("Ken")
//                            .setLastName("Shimauchi")
//                                    .build();
//    // Create a greetRequest and set the message
//    GreetRequest greetRequest = GreetRequest.newBuilder()
//                    .setGreeting(greeting)
//                            .build();
//    // Create a greetResponse and pass the greetClient a greetRequest
//    GreetResponse greetResponse = greetClient.greet(greetRequest);
//
//
//    // Print response
//    System.out.println(greetResponse.getResult());

    // Server Streaming
    GreetManyTimesRequest greetManyTimesRequest =
            GreetManyTimesRequest.newBuilder()
                            .setGreeting(Greeting.newBuilder()
                                    .setFirstName("Ken"))
                                    .build();

    greetClient
        .greetManyTimes(greetManyTimesRequest)
        .forEachRemaining(
            greetManyTimesResponse -> {
              System.out.println(greetManyTimesResponse.getResult());
            });

    System.out.println("Shutting down channel");

    //For a async Client
    //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);
    channel.shutdown();
  }
}
