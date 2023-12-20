package ait.chat.server.task;

import ait.mediation.BlkQueue;
import ait.mediation.BlkQueueImpl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatServerSender implements Runnable {
    private BlkQueue<String> box;
    private List<PrintWriter> printWriters;

    public ChatServerSender(BlkQueue<String> box) {
        this.box = box;
        this.printWriters = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            String message = box.pop();
            synchronized (this) {
                Iterator<PrintWriter> iterator = printWriters.iterator();
                while (iterator.hasNext()) {
                    PrintWriter clientWriter = iterator.next();
                    if (clientWriter.checkError()) {
                        iterator.remove();
                    } else {
                        clientWriter.println(message);
                    }
                }
            }
        }
    }

    public void addPrintWriter(Socket socket) throws IOException {
        synchronized (this) {
            printWriters.add(new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true));
        }
    }
}
