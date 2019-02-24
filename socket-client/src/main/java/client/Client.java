package client;

import client.handlers.AsyncConnectionHandler;
import client.handlers.AsyncReadHandler;
import client.handlers.AsyncWriteHandler;
import common.AsyncClientSocket;
import common.Message;
import common.MessageConverter;
import common.ResponseMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Client extends AsyncClientSocket {
    private final List<ResponseMessage> messages = new ArrayList<>(0);
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

    public void write(Message message) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(MessageConverter.convertToBytes(message));
        buffer.flip();
        channel.write(buffer, channel, new AsyncWriteHandler());
    }

    public void startRead() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer, byteBuffer, new AsyncReadHandler(this));
    }

    public void addMessage(ResponseMessage message) {
        messages.add(message);
        System.out.println(format("ResponseMessage received successfully: {%s} result {%s}", message.Request, message.Data));
    }

    public List<ResponseMessage> getMessages() {
        return List.copyOf(messages);
    }
}
