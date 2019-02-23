package server.handlers;

import common.AsyncClientSocket;
import server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AsyncReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsyncClientSocket client;
    private final Server server;

    public AsyncReadHandler(Server server, AsyncClientSocket client) {
        this.client = client;
        this.server = server;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        server.startReading(client);
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        String message = new String(bytes, StandardCharsets.UTF_8);
        server.startHandlingMessage(client, message);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
        try {
            client.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
