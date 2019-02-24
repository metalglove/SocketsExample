package server;

import common.*;
import server.handlers.*;

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
        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(100));
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

    public void startWriting(AsyncClientSocket client, ResponseMessage message) throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(MessageConverter.convertToBytes(message));
        byteBuffer.flip();
        client.channel.write(byteBuffer, byteBuffer, new AsyncWriteHandler(client));
    }

    public void startReading(AsyncClientSocket client) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        client.channel.read(byteBuffer, byteBuffer, new AsyncReadHandler(this, client));
    }

    public void startHandlingMessage(AsyncClientSocket client, byte[] message) throws IOException, ClassNotFoundException, UnknownMessageTypeException {
        Object object = MessageConverter.convertFromBytes(message);
        if (object instanceof RequestMessage)
            startProcessingRequest(client, (RequestMessage)object);
        else if (object instanceof CommandMessage)
            startProcessingCommand(client, (CommandMessage)object);
        else
            throw new UnknownMessageTypeException();
    }

    public void startProcessingRequest(AsyncClientSocket client, RequestMessage message) {
        if (message.Request.equals(Requests.GET_CLIENT_COUNT)) {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.Data = String.valueOf(clients.size());
            System.out.println(responseMessage.Data);
            responseMessage.Success = true;
            responseMessage.Request = Requests.GET_CLIENT_COUNT;
            new AsyncRequestMessageHandler(this, client).completed(responseMessage, message);
        } else {
            new AsyncRequestMessageHandler(this, client).failed(new Exception("Something unforeseen happened!"), message);
        }
    }

    public void startProcessingCommand(AsyncClientSocket client, CommandMessage message) {
        if (message.Command.equals(Commands.DO_SOMETHING)) {
            System.out.println("I did something!");
            new AsyncCommandMessageHandler(this, client).completed(null, message);
        } else {
            new AsyncCommandMessageHandler(this, client).failed(new Exception("Something unforeseen happened!"), message);
        }
    }
}
