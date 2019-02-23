package server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server(9999);
        try {
            server.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
