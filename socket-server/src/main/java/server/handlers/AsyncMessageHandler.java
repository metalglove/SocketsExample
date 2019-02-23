package server.handlers;

import common.AsyncClientSocket;
import server.Server;

import java.nio.channels.CompletionHandler;

public class AsyncMessageHandler implements CompletionHandler<String, Void> {
    private final Server server;
    private final AsyncClientSocket client;

    public AsyncMessageHandler(Server server, AsyncClientSocket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void completed(String result, Void attachment) {
        System.out.println("Message received: " + result);
        server.startWriting(client, "Hello client");
    }

    @Override
    public void failed(Throwable exc, Void attachment) {

    }
}
