package com.github.simple.grpc.calculator.client;

import com.github.simple.grpc.greeting.client.GreetingClient;
import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class CalculatorClient {
    private void run(){
        // Create a channel on given domain and port we will later be able to use ssl
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50052)
                .usePlaintext()
                .build();
        //doUnaryCall(channel);
        //doServerStreamingCall(channel);
        //doClientStreamCall(channel);
        doClientStreamCall(channel);

        System.out.println("Shutting down channel ");

        channel.shutdown();
    }

    private void doUnaryCall(ManagedChannel channel){
        // Create the stub for the CalculatorServiceGrpc
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        //Unary
        // Create a request for the Calculator Service
        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(10)
                .setSecondNumber(20)
                .build();

        SumResponse response = stub.sum(request);

        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() + " = " + response.getSumResult() );

    }

    private void doServerStreamingCall(ManagedChannel channel){
        // Create the stub for the CalculatorServiceGrpc
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        // Streaming Server
        Long number = 5678901285453333201L;
        stub.primeNumberDecomposition(
                        PrimeNumberDecompositionRequest.newBuilder().setNumber(number).build())
                .forEachRemaining(
                        primeNumberDecompositionResponse -> {
                            System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
                        });
    }

    private void doClientStreamCall(ManagedChannel channel){
        // Create the stub for the CalculatorServiceGrpc
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Received Response from server ");
                System.out.println(value.getAverage());
            }
            @Override
            public void onError(Throwable t) {

            }
            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us data");
                latch.countDown();
            }
        });
//
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(1)
//                .build());
//
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(2)
//                .build());
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(3)
//                .build());
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(4)
//                .build());
//        //expect the average 10 /4 = 2.5

        for(int i = 0; i < 9999; i++){
            requestObserver.onNext(ComputeAverageRequest.newBuilder()
                    .setNumber(i)
                    .build());
        }
        requestObserver.onCompleted();

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        System.out.println("Hello I am a grpc client");

        CalculatorClient main = new CalculatorClient();
        main.run();


    }
}