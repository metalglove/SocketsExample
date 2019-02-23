package client.handlers;

import client.Client;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AsyncReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final Client client;

    public AsyncReadHandler(Client client) {
        this.client = client;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        client.startRead();
        System.out.println("Successfully read message from server!");
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        String message = new String(bytes, StandardCharsets.UTF_8);
        client.addMessage(message);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("Failed to read message from server!");
    }
}
