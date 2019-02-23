package client.handlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncConnectionHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    @Override
    public void completed(Void result, AsynchronousSocketChannel attachment) {
        System.out.println("Connected successfully!");
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        System.out.println("Failed to connect!");
    }
}