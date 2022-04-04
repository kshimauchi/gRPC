package com.github.simple.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import com.proto.calculator.Calculator;
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

        // Create a request for the Calculator Service
        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(10)
                .setSecondNumber(20)
                .build();

        SumResponse response = stub.sum(request);

        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() + " = " + response.getSumResult() );

        // Shut down the channel
        channel.shutdown();
    }
}