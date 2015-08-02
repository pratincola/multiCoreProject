package hw4.q1;

import edu.multicore.queues.MyQueue;

import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue<T> implements MyQueue<T> {
    AtomicInteger count = new AtomicInteger(0);
    ReentrantLock enq_lock, deq_lock;
    Node<T> head;

    Node<T> tail;
    int emptyCounter;

    public LockQueue() {
        head = new Node<>(null);
        tail = head;
        enq_lock = new ReentrantLock();
        deq_lock = new ReentrantLock();
        emptyCounter = 0;
    }

    public int getEmptyCounter() {
        return emptyCounter;
    }

    public boolean enq(T value) {
        if (value == null) throw new NullPointerException();
        enq_lock.lock();
        try{
            Node<T> new_element = new Node<>(value);
            tail.next = new_element;
            tail = new_element;
        }finally {
            enq_lock.unlock();
        }
        return true;
    }

    public T deq() throws EmptyStackException{
        T return_val;
        deq_lock.lock();
        try{
            if (head.next == null) {
                emptyCounter++;
                throw new EmptyStackException();
            }
            return_val = head.next.value;
            head = head.next;
        } finally {
            deq_lock.unlock();
        }
        return return_val;
    }

    private static class Node<T>{
        final T value;
        volatile Node<T> next;
        Node(T value){
            this.value = value;
        }
    }
}
