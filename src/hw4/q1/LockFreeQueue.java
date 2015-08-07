package hw4.q1;


import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.EmptyException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> implements MyQueue<T> {
    private AtomicReference<LockFreeQueue.Node> head, tail;
    private Node dummy;

    AtomicInteger count = new AtomicInteger(0);
    private int capacity;

    public LockFreeQueue(){
        dummy = new LockFreeQueue.Node(null);
        head = new AtomicReference<LockFreeQueue.Node>(dummy);
        tail = new AtomicReference<LockFreeQueue.Node>(dummy);
        capacity = -1;
    }

    public LockFreeQueue(int capacity) {
        this();
        this.capacity = capacity;
    }

    public void init(){
        dummy = new LockFreeQueue.Node(null);
        head = new AtomicReference<LockFreeQueue.Node>(dummy);
        tail = new AtomicReference<LockFreeQueue.Node>(dummy);
        count.set(0);
    }

    @Override
    public boolean enq(T value) {


        Node node = new Node(value);
        Node tail = this.tail.get();

        while(true){
            tail = this.tail.get();
            Node next = tail.next.get();
            if(this.tail.get() == tail){
                if(next == null){
                    if(tail.next.compareAndSet(next, node)){
                        break;
                    }
                }
                else{
                    this.tail.compareAndSet(tail, next);
                }
            }
        }
        return this.tail.compareAndSet(tail, node);
    }

    @Override
    public T deq() {

        while(true){
            Node first = head.get();
            Node last = tail.get();
            Node next = null;
            if(first.next != null)
                next = first.next.get();
            if(first == head.get()){
                if(first == last){
                    if(next == null){
                      return null;        // return null or throw EmptyException..
                    }
                    tail.compareAndSet(last, next);
                }
                else{
                    T value = null;
                    if(next != null)
                        value = next.value;
                    if(next == null)
                        next = dummy;
                    if(head.compareAndSet(first, next)){
                        if(capacity > 0) {
                            if (count.decrementAndGet() < 0) {
//                                count.set(0);
                            }
                        }
                        first.next.set(null);
                        return value;
                    }
                }
            }
        }
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return (head.get().next.get() == null);
    }

    public class Node {

        public T value;
        public AtomicReference<Node> next;

        public Node(T value) {
            this.value = value;
            this.next = new AtomicReference<Node>(null);
        }
    }
    public static void main(String[] args) {
        LockFreeQueue l = new LockFreeQueue();
        System.out.println(l.head);
        System.out.println(l.tail);
        System.out.println(l.enq(5));
        System.out.println(l.enq(6));
        System.out.println(l.enq(7));
        System.out.println(l.enq(8));
        System.out.println(l.deq());
        System.out.println(l.deq());
        System.out.println(l.enq(9));
        System.out.println(l.enq(10));
        System.out.println(l.deq());
        System.out.println(l.deq());
        System.out.println(l.deq());

    }

}