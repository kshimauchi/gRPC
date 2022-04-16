package com.github.simple.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        SumResponse sumResponse = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber()+ request.getSecondNumber())
                .build();

        responseObserver.onNext(sumResponse);

        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        Long number = request.getNumber();
        Long divisor = 2L;
        while(number > 1){
            if(number % divisor==0){
                number = number /divisor;
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(divisor)
                        .build());
            }else{
                divisor++;
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {

        StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {
            //running sum and count
            int sum = 0;
            int count = 0;


            /**
             * Receives a value from the stream.
             *
             * <p>Can be called many times but is never called after {@link #onError(Throwable)} or {@link
             * #onCompleted()} are called.
             *
             * <p>Unary calls must invoke onNext at most once.  Clients may invoke onNext at most once for
             * server streaming calls, but may receive many onNext callbacks.  Servers may invoke onNext at
             * most once for client streaming calls, but may receive many onNext callbacks.
             *
             * <p>If an exception is thrown by an implementation the caller is expected to terminate the
             * stream by calling {@link #onError(Throwable)} with the caught exception prior to
             * propagating it.
             *
             * @param value the value passed to the stream
             */
            @Override
            public void onNext(ComputeAverageRequest value) {
                //increment the sum
                sum += value.getNumber();
                //increment the count
                count++;
            }

            /**
             * Receives a terminating error from the stream.
             *
             * <p>May only be called once and if called it must be the last method called. In particular if an
             * exception is thrown by an implementation of {@code onError} no further calls to any method are
             * allowed.
             *
             * <p>{@code t} should be a {@link StatusException} or {@link
             * StatusRuntimeException}, but other {@code Throwable} types are possible. Callers should
             * generally convert from a {@link Status} via {@link Status#asException()} or
             * {@link Status#asRuntimeException()}. Implementations should generally convert to a
             * {@code Status} via {@link Status#fromThrowable(Throwable)}.
             *
             * @param t the error occurred on the stream
             */
            @Override
            public void onError(Throwable t) {

            }

            /**
             * Receives a notification of successful stream completion.
             *
             * <p>May only be called once and if called it must be the last method called. In particular if an
             * exception is thrown by an implementation of {@code onCompleted} no further calls to any method
             * are allowed.
             */
            @Override
            public void onCompleted() {
                double average = (double) sum/count;
                responseObserver.onNext(
                        ComputeAverageResponse.newBuilder()
                                .setAverage(average)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}
