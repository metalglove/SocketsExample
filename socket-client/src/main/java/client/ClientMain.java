package client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException, IOException {
        Client client = new Client("127.0.0.1", 9999);
        client.connect();
        Thread.sleep(4000);
        client.startRead();
        client.write("Hello server");
        Thread.sleep(10);
        client.write("Hello server");
        Thread.sleep(10);
        client.write("Hello server");
        Thread.sleep(4000000);
    }
}
