package ait.chat.client;

import ait.chat.client.task.MessageReceiver;
import ait.chat.client.task.MessageSender;

import java.io.IOException;
import java.net.Socket;

public class ChatClientAppl {
    public static void main(String[] args) {
        String serverHost = "5.tcp.eu.ngrok.io"; // localhost
        int port = 17250;
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
