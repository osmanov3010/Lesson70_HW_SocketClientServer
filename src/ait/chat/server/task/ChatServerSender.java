package ait.chat.server.task;

import ait.mediation.BlkQueueImpl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ChatServerSender implements Runnable {
    private BlkQueueImpl<String> box;
    private List<PrintWriter> printWriters;

    public ChatServerSender(BlkQueueImpl<String> box) {
        this.box = box;
        this.printWriters = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            String message = box.pop();
            for (PrintWriter printWriter : printWriters) {
                printWriter.println(message);
                printWriter.flush();
            }
        }
    }

    public void addPrintWriter(PrintWriter printWriter) {
        printWriters.add(printWriter);
    }
}
