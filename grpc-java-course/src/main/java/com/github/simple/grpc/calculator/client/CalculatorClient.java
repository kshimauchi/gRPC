package com.github.simple.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class CalculatorClient {
    public static void main(String [] args){
        // Create a channel on given domain and port we will later be able to use ssl
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50052)
                .usePlaintext()
                .build();

        // Create the stub for the CalculatorServiceGrpc
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
// Unary
//        // Create a request for the Calculator Service
//        SumRequest request = SumRequest.newBuilder()
//                .setFirstNumber(10)
//                .setSecondNumber(20)
//                .build();
//
//        SumResponse response = stub.sum(request);
//
//        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() + " = " + response.getSumResult() );

        // Streaming Server
        Long number = 5678901285453333201L;
        stub.primeNumberDecomposition(
            PrimeNumberDecompositionRequest.newBuilder().setNumber(number).build())
        .forEachRemaining(
            primeNumberDecompositionResponse -> {
              System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
            });

        // Shut down the channel
        channel.shutdown();
    }
}