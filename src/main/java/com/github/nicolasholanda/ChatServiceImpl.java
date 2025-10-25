package com.github.nicolasholanda;

import com.google.protobuf.Timestamp;
import dk.adamino.grpc.chat.Chat;
import dk.adamino.grpc.chat.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.LinkedHashSet;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private static LinkedHashSet<StreamObserver<Chat.ChatMessageFromServer>> observers = new LinkedHashSet<>();

    @Override
    public StreamObserver<Chat.ChatMessage> sendChatMessage(StreamObserver<Chat.ChatMessageFromServer> responseObserver) {
        // Add response observer to the list
        observers.add(responseObserver);

        // Handler for client messages
        return new StreamObserver<>() {

            @Override
            public void onNext(Chat.ChatMessage value) {

                System.out.println(value);

                // Create a server message from the client message
                Timestamp timestamp = Timestamp.newBuilder()
                        .setSeconds(System.currentTimeMillis())
                        .build();

                Chat.ChatMessageFromServer message = Chat.ChatMessageFromServer
                        .newBuilder()
                        .setMessage(value)
                        .setTimestamp(timestamp)
                        .build();

                // Notify all observers
                for (StreamObserver<Chat.ChatMessageFromServer> observer : observers) {
                    System.out.println("Sent message to: " + observer.toString());
                    observer.onNext(message);
                }
            }

            @Override
            public void onError(Throwable t) {
                t.getMessage();
                observers.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed!");
            }
        };

    }
}
