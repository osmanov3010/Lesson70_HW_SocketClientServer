package ait.mediation;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlkQueueImpl<T> implements BlkQueue<T> {

    private int maxSize;
    private LinkedList<T> list;
    Lock mutex = new ReentrantLock();
    Condition pushWaitingCondition = mutex.newCondition();
    Condition popWaitingCondition = mutex.newCondition();

    public BlkQueueImpl(int maxSize) {
        list = new LinkedList<>();
        this.maxSize = maxSize;
    }

    @Override
    public void push(T message) {
        // add
        mutex.lock();
        try {
            while (list.size() == maxSize) {
                try {
                    pushWaitingCondition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            list.push(message);
            popWaitingCondition.signal();
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public T pop() {
        // get
        mutex.lock();
        try {
            while (list.isEmpty()) {
                try {
                    popWaitingCondition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            pushWaitingCondition.signal();
            return list.pop();
        } finally {
            mutex.unlock();
        }
    }
}
