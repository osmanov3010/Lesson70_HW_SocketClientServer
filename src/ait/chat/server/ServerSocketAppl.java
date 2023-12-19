package ait.chat.server;

import ait.chat.server.task.ChatServerReceiver;
import ait.chat.server.task.ChatServerSender;
import ait.chat.server.task.ClientHandler;
import ait.mediation.BlkQueueImpl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketAppl {
    public static void main(String[] args) {
        int port = 9000;
        BlkQueueImpl<String> box = new BlkQueueImpl<>(200); // messageBoxContainer


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ChatServerSender chatServerSender = new ChatServerSender(box);
            Thread chatServerSenderThread = new Thread(chatServerSender);
            chatServerSenderThread.setDaemon(true);
            chatServerSenderThread.start();

            try {
                while (true) {
                    System.out.println("Server is waiting for connection...");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connection established with client host : " + socket.getInetAddress() + ":" + socket.getPort());
                    chatServerSender.addPrintWriter(new PrintWriter(new OutputStreamWriter(socket.getOutputStream())));
                    Thread chatServerReceiver = new Thread(new ChatServerReceiver(socket, box));
                    chatServerReceiver.start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}