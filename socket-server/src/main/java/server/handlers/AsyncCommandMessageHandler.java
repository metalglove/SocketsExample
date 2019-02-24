package server.handlers;

import common.AsyncClientSocket;
import common.CommandMessage;
import server.Server;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncCommandMessageHandler implements CompletionHandler<Void, CommandMessage> {
    private final Server server;
    private final AsyncClientSocket client;

    public AsyncCommandMessageHandler(Server server, AsyncClientSocket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void completed(Void result, CommandMessage attachment) {
        try {
            System.out.println(format("CommandMessage executed successfully: {%s} by {%s}", attachment.Command, client.channel.getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, CommandMessage attachment) {
        try {
            System.out.println(format("CommandMessage executed unsuccessfully: {%s} by {%s}", attachment.Command, client.channel.getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
