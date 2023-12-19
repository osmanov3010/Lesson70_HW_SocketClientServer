package ait.chat.server;

import ait.chat.server.task.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerSocketApplBackup {
    public static void main(String[] args) {
        int port = 9000;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                while (true) {
                    System.out.println("server wait...");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connection established");
                    System.out.println("Client host: " + socket.getInetAddress() + ":" + socket.getPort());
                    ClientHandler clientHandler = new ClientHandler(socket);
                    executorService.execute(clientHandler);
                }
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}