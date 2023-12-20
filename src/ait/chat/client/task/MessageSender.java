package ait.chat.client.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageSender implements Runnable {

    private Socket socket;

    public MessageSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {
            PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your name");
            String name = br.readLine();
            socketWriter.println(name + " has joined the chat");
            socketWriter.flush();
            System.out.println("Enter your message or type exit for quit");
            String message = br.readLine();
            while (!"exit".equalsIgnoreCase(message)) {
                socketWriter.println(name + " [" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + "] " + message);
                socketWriter.flush();
                message = br.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
