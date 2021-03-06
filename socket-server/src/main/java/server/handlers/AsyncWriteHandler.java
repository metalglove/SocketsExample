package server.handlers;

import common.AsyncClientSocket;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncWriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsyncClientSocket client;

    public AsyncWriteHandler(AsyncClientSocket client) {
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(attachment.hasRemaining()){
            client.channel.write(attachment, attachment, this);
        } else {
            System.out.println("Successfully sent ResponseMessage!");
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("Failed to send ResponseMessage!");
        exc.printStackTrace();
    }
}
