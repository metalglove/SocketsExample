package server.handlers;

import common.AsyncClientSocket;
import server.Server;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {
    @Override
    public void completed(AsynchronousSocketChannel result, Server server) {
        System.out.println("Accepted a new connection!");
        // start accepting the next connection
        server.startAccepting();

        // register client
        AsyncClientSocket client = new AsyncClientSocket(result);
        server.registerClient(client);

        // start reading the socket channel for data
        server.startReading(client);
    }

    @Override
    public void failed(Throwable exc, Server attachment) {
        System.out.println("Failed to accept a new connection!");
        exc.printStackTrace();
    }
}
