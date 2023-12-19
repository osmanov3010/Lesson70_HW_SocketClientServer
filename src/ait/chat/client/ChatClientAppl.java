package ait.chat.client;

import ait.chat.client.task.MessageReceiver;
import ait.chat.client.task.MessageSender;

import java.io.IOException;
import java.net.Socket;

public class ChatClientAppl {
    public static void main(String[] args) {
        String serverHost = "127.0.0.1"; // localhost
        int port = 9000;
        try {
            Socket socket = new Socket(serverHost, port);
            Thread sender = new Thread(new MessageSender(socket));
            Thread receiver = new Thread(new MessageReceiver(socket));
            receiver.setDaemon(true);
            receiver.start();
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
