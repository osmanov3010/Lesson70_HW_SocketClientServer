package ait.chat.server.task;

import ait.mediation.BlkQueueImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class ChatServerReceiver implements Runnable {
    private Socket socket;
    private BlkQueueImpl<String> box;

    public ChatServerReceiver(Socket socket, BlkQueueImpl<String> box) {
        this.socket = socket;
        this.box = box;
    }

    @Override
    public void run() {

        try (Socket socket = this.socket) {
            InputStream inputStream = socket.getInputStream();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(inputStream));

            while (true) {
                String message = socketReader.readLine();
                if (message == null) {
                    break;
                }
                System.out.println(message);
                box.push(message);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
