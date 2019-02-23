package server;

import java.nio.channels.CompletionHandler;

public class MessageDecoder {
    public void decode(String message, CompletionHandler<String, Void> handler) {
        if ("Hello server".equals(message)) {
            handler.completed(message, null);
        } else {
            handler.failed(new Exception("Message could not be decoded!"), null);
        }
    }
}
