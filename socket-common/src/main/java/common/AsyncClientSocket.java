package common;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class AsyncClientSocket {
    public final AsynchronousSocketChannel channel;

    public AsyncClientSocket() throws IOException {
        channel = AsynchronousSocketChannel.open();
    }

    public AsyncClientSocket(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

}