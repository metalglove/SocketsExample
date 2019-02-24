package client.handlers;

import client.Client;
import common.MessageConverter;
import common.ResponseMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final Client client;

    public AsyncReadHandler(Client client) {
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        client.startRead();
        System.out.println("Successfully read message from server!");
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try {
            ResponseMessage message = (ResponseMessage)MessageConverter.convertFromBytes(bytes);
            client.addMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to read message from server (while converting)!");
            e.printStackTrace();
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("Failed to read message from server!");
    }
}
