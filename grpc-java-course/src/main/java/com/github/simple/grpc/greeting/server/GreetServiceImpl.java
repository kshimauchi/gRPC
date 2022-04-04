package com.github.simple.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        //extract the fields we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        String result = "Hello " + firstName;

        //Create Response
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        //Send to client by using responseObserver
        responseObserver.onNext(response);

        //Complete the rpc call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        try {

            for(int i = 0 ; i < 10; i++) {
                String result = "Hello " + firstName + ", response number " + i;

                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                    .setResult(result)
                            .build();

                responseObserver.onNext(response);

                Thread.sleep(1000L);
            }

        } catch (InterruptedException e) {
                e.printStackTrace();
        } finally {
                responseObserver.onCompleted();
        }
    }

}

