package client.handlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncWriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {
        System.out.println("Successfully send message to server!");
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        System.out.println("Failed to send message to server!");
    }
}
