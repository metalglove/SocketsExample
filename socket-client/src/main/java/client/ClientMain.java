package client;

import common.CommandMessage;
import common.Commands;
import common.RequestMessage;
import common.Requests;

import java.io.IOException;
import java.util.Random;

public class ClientMain {
    public static Random RANDOM = new Random(System.nanoTime());

    public static void main(String[] args) throws IOException, InterruptedException {
        int i = 0;
        while (i < 40) {
            i++;
            Client client = new Client("127.0.0.1", 9999);
            client.connect();
            Thread.sleep(100);
            client.startRead();
            CommandMessage commandMessage = new CommandMessage();
            commandMessage.Command = Commands.DO_SOMETHING;
            commandMessage.Data = "Hello server";
            client.write(commandMessage);
            Thread.sleep(10);
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.Request = Requests.GET_CLIENT_COUNT;
            requestMessage.Data = "";
            requestMessage.Id = RANDOM.nextInt(999999);
            client.write(requestMessage);
        }
        Thread.sleep(100000);
    }
}
