package edu.multicore.queues.unbounded;

/**
 * Created by pratik1 on 7/27/15.
 */

import java.util.concurrent.locks.ReentrantLock;

import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.EmptyException;
import edu.multicore.queues.utils.Node;
public class UnboundedQueue<T> implements MyQueue{

    ReentrantLock enqLock, deqLock;
    Node<Integer> head;
    Node<Integer> tail;
    int size;
    public UnboundedQueue() {
        head = new Node<Integer>(null);
        tail = head;
        enqLock = new ReentrantLock();
        deqLock = new ReentrantLock();
    }


    public Integer deq() {
        Integer result = null;
        deqLock.lock();
        try {
            if (head.next == null) {
                throw new EmptyException("empty");
            }
            result = head.next.value;
            head = head.next;
        } catch (EmptyException e) {
            e.printStackTrace();
        } finally {
            deqLock.unlock();
        }
        return result;
    }

    public boolean enq(Object value) {
        if (value == null) throw new NullPointerException();
        enqLock.lock();
        try {
            Node<Integer> e = new Node<Integer>((Integer)value);
            tail.next = e;
            tail = e;
        } finally {
            enqLock.unlock();
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}