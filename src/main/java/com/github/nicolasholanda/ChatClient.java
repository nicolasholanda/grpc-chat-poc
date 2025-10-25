package com.github.nicolasholanda;

import dk.adamino.grpc.chat.Chat;
import dk.adamino.grpc.chat.ChatServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        ChatServiceGrpc.ChatServiceStub stub = ChatServiceGrpc.newStub(channel);

        StreamObserver<Chat.ChatMessage> requestObserver = stub.sendChatMessage(
                new StreamObserver<>() {
                    @Override
                    public void onNext(Chat.ChatMessageFromServer value) {
                        System.out.println("Server: " + value.getMessage());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Error: " + t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Chat was finished");
                    }
                });

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            Chat.ChatMessage message = Chat.ChatMessage.newBuilder()
                    .setMessage(input)
                    .build();
            requestObserver.onNext(message);
        }

        requestObserver.onCompleted();
        channel.shutdown();
    }
}