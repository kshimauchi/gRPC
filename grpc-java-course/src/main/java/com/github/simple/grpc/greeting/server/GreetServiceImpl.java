package com.github.simple.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.stream.Stream;

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
    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        //Client Stream returns a StreamObserver so 1) need to implement a new StreamObserver
        //Since, this is asynchronous we need to handle how we want to unload requests
        StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<LongGreetRequest>(){

            String result = "";
            @Override
            public void onNext(LongGreetRequest value) {
                //client sends a message
                result += "Hello " + value.getGreeting().getFirstName() + " !";
            }
            @Override
            public void onError(Throwable t) { }

            @Override
            public void onCompleted() {
                //client is done
                responseObserver.onNext(
                        LongGreetResponse.newBuilder()
                            .setResult(result)
                            .build()
                );
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}

