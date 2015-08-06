package hw4.q1;

import edu.multicore.queues.MyQueue;

import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue<T> implements MyQueue<T> {

    ReentrantLock enq_lock, deq_lock;
    Node<T> head;

    Node<T> tail;
    int emptyCounter;
    AtomicInteger count = new AtomicInteger(0);
    private int capacity;

    public LockQueue() {
        head = new Node<>(null);
        tail = head;
        enq_lock = new ReentrantLock();
        deq_lock = new ReentrantLock();
        emptyCounter = 0;
        capacity = -1;
    }
    public LockQueue(int capacity) {
        this();
        this.capacity = capacity;
    }

    public int getEmptyCounter() {
        return emptyCounter;
    }

    public boolean enq(T value) {
        if (value == null) throw new NullPointerException();
        if(capacity > 0 && count.get() > capacity)
            return false;

        enq_lock.lock();
        try{
            Node<T> new_element = new Node<>(value);
            tail.next = new_element;
            tail = new_element;
            if(capacity > 0)
                count.incrementAndGet();
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
                return null;
            }
            return_val = head.next.value;
            head = head.next;
            if(return_val != null){
                if(capacity > 0) {
                    count.decrementAndGet();
                    if (count.get() < 0) {
                        count.set(0);
                    }
                }
            }
        } finally {
            deq_lock.unlock();
        }
        return return_val;
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return head.next == null;
    }

    private static class Node<T>{
        final T value;
        volatile Node<T> next;
        Node(T value){
            this.value = value;
        }
    }
}
