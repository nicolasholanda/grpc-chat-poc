# gRPC Chat POC

A proof of concept for a bidirectional streaming chat application using gRPC and Java.

## Overview

This project demonstrates a simple chat server and client built with gRPC. Multiple clients can connect simultaneously and exchange messages in real-time using bidirectional streaming.

## Requirements

- Java 21
- Maven 3.6+

## Building

```bash
mvn clean package
```

## Running

Start the server:

```bash
java -cp target/grpc-chat-poc-1.0-SNAPSHOT.jar com.github.nicolasholanda.ChatServer
```

In another terminal, start the client:

```bash
java -cp target/grpc-chat-poc-1.0-SNAPSHOT.jar com.github.nicolasholanda.ChatClient
```

Type messages in the client terminal to chat. You can run multiple clients simultaneously.

## Architecture

- **ChatServer**: Listens on port 9090 and handles incoming client connections
- **ChatClient**: Connects to the server and sends/receives messages interactively
- **ChatServiceImpl**: Implements the gRPC service with bidirectional streaming logic
- **chat.proto**: Protocol Buffer definition for message types

Messages are broadcasted to all connected clients through a shared set of stream observers.

