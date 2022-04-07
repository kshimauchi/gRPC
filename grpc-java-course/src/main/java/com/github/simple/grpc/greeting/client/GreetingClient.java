package com.github.simple.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {
  ManagedChannel channel;

  public void run(){
    channel = ManagedChannelBuilder.forAddress(
            "localhost",50051
            ).usePlaintext()
            .build();
//    doUnaryCall(channel);
//
//    doServerStreamingCall(channel);

    doClientStreamingCall(channel);

    System.out.println("Creating Stub");
  }

  private void doUnaryCall(ManagedChannel channel){
    // created a greeting service client (blocking - synchronous)
    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

    // created a protocol buffer greeting message
    Greeting greeting = Greeting.newBuilder()
                    .setFirstName("Ken")
                            .setLastName("Shimauchi")
                                    .build();
    // Create a greetRequest and set the message
    GreetRequest greetRequest = GreetRequest.newBuilder()
                    .setGreeting(greeting)
                            .build();
    // Create a greetResponse and pass the greetClient a greetRequest
    GreetResponse greetResponse = greetClient.greet(greetRequest);

    // Print response
    System.out.println(greetResponse.getResult());

    System.out.println("Shutting down channel");
    channel.shutdown();
  }

  private void doServerStreamingCall(ManagedChannel channel){
    // created a greeting service client (blocking - synchronous)
    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

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
    channel.shutdown();

  }

  private void doClientStreamingCall(ManagedChannel channel) {
    // create an asynchronous client stub
    GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<LongGreetRequest> requestObserver =
            asyncClient.longGreet(
            new StreamObserver<LongGreetResponse>() {

              @Override
              public void onNext(LongGreetResponse value) {
                System.out.println("Received a response from the server ");
                System.out.println(value.getResult());
              }

              @Override
              public void onError(Throwable t) {}

              @Override
              public void onCompleted() {
                System.out.println("Server has completed sending us something");
                latch.countDown();
              }
            });
    // STREAMING M1
    System.out.println("");
    requestObserver.onNext(LongGreetRequest.newBuilder()
            .setGreeting(Greeting.newBuilder()
                    .setFirstName("Ken")
                    .build())
            .build());
    // STREAMING M2
    requestObserver.onNext(LongGreetRequest.newBuilder()
            .setGreeting(Greeting.newBuilder()
                    .setFirstName("John")
                    .build())
            .build());
    // STREAMING M3
    requestObserver.onNext(LongGreetRequest.newBuilder()
            .setGreeting(Greeting.newBuilder()
                    .setFirstName("Marc")
                    .build())
            .build());
    // CALL ONCOMPLETED
    requestObserver.onCompleted();

    // TRY CATCH FOR THE LATCH
    try{
      latch.await(3L, TimeUnit.SECONDS);

    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    System.out.println("Hello I am a grpc client");

    GreetingClient main = new GreetingClient();
    main.run();
  }
}