package com.github.simple.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
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

    DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

    System.out.println("Shutting down channel");

    //For a async Client
    //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);
    channel.shutdown();
  }
}
