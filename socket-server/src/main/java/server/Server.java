package server;

import common.AsyncClientSocket;
import server.handlers.AsyncAcceptHandler;
import server.handlers.AsyncMessageHandler;
import server.handlers.AsyncReadHandler;
import server.handlers.AsyncWriteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    private final AsynchronousServerSocketChannel server;
    private final AsynchronousChannelGroup group;
    private final List<AsyncClientSocket> clients = new CopyOnWriteArrayList<>();

    public Server(int port) throws IOException {
        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(10));
        server = AsynchronousServerSocketChannel.open(group);
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 1024);
        server.bind(new InetSocketAddress(port), 100);
        System.out.println("Started server socket on: " + server.getLocalAddress().toString());
        startAccepting();
    }

    public void await() throws InterruptedException {
        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    public void registerClient(AsyncClientSocket client) {
        clients.add(client);
    }

    public void unRegisterClient(AsyncClientSocket client) {
        clients.remove(client);
    }

    public void startAccepting() {
        server.accept(this, new AsyncAcceptHandler());
    }

    public void startWriting(AsyncClientSocket client, String message) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();
        client.channel.write(byteBuffer, byteBuffer, new AsyncWriteHandler(client));
    }

    public void startReading(AsyncClientSocket client) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        client.channel.read(byteBuffer, byteBuffer, new AsyncReadHandler(this, client));
    }

    public void startHandlingMessage(AsyncClientSocket client, String message) {
        MessageDecoder messageDecoder = new MessageDecoder();
        messageDecoder.decode(message, new AsyncMessageHandler(this, client));
    }
}
