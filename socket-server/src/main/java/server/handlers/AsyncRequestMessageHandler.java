package server.handlers;

import common.AsyncClientSocket;
import common.RequestMessage;
import common.ResponseMessage;
import server.Server;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncRequestMessageHandler implements CompletionHandler<ResponseMessage, RequestMessage> {
    private final Server server;
    private final AsyncClientSocket client;

    public AsyncRequestMessageHandler(Server server, AsyncClientSocket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void completed(ResponseMessage result, RequestMessage attachment) {
        try {
            System.out.println(format("RequestMessage executed successfully: {%s} by {%s}", attachment.Request, client.channel.getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.startWriting(client, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, RequestMessage attachment) {
        try {
            System.out.println(format("RequestMessage executed unsuccessfully: {%s} by {%s}", attachment.Request, client.channel.getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}