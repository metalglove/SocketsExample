package client;

import client.handlers.AsyncConnectionHandler;
import client.handlers.AsyncReadHandler;
import client.handlers.AsyncWriteHandler;
import common.AsyncClientSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Client extends AsyncClientSocket {

    private final List<String> messages = new ArrayList<>(0);
    private final String host;
    private final int port;

    public Client(String host, int port) throws IOException {
        super();
        this.host = host;
        this.port = port;
    }

    public void connect() {
        channel.connect(new InetSocketAddress(host, port), channel, new AsyncConnectionHandler());
    }

    public void write(String message) {
        final ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.put(message.getBytes());
        buffer.flip();
        channel.write(buffer, channel, new AsyncWriteHandler());
    }

    public void startRead() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer, byteBuffer, new AsyncReadHandler(this));
    }

    public void addMessage(String message) {
        messages.add(message);
        System.out.println("Message received: " + message);
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }
}
