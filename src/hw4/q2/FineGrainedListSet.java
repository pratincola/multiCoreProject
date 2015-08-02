package hw4.q2;

import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedListSet<T> implements ListSet<T> {
    private Node head;
    public FineGrainedListSet(){
        head = new Node<>(Integer.MIN_VALUE);
        head.next= new Node<>(Integer.MAX_VALUE);
    }
    public boolean add(T value) {
        int add_key = value.hashCode();
        head.lock.lock();
        Node predecessor = head;
        try {
            Node current = predecessor.next;
            current.lock.lock();
            try {
                while (current.key < add_key){
                    predecessor.lock.unlock();
                    predecessor = current;
                    current = current.next;
                    current.lock.lock();
                }
                if (current.key == add_key)
                    return false;
                Node new_addition = new Node<>(value);
                new_addition.next = current;
                predecessor.next = new_addition;
                return true;
            } finally {
                current.lock.unlock();
            }
        } finally {
            predecessor.lock.unlock();
        }
    }
    public boolean remove(T value) {
        int rem_key = value.hashCode();
        head.lock.lock();
        Node predecessor = head;
        try {
            Node current = predecessor.next;
            current.lock.lock();
            try {
                //Traverse down the sorted list till you find a key equal to or greater than your own
                while (current.key < rem_key) {
                    predecessor.lock.unlock();
                    predecessor = current;
                    current = current.next;
                    current.lock.lock();
                }
                //If you find the key remove it
                if (current.key == rem_key) {
                    predecessor.next = current.next;
                    return true;
                }
                //Else no such key exists
                return false;
            } finally {
                current.lock.unlock();
            }
        } finally {
            predecessor.lock.unlock();
        }
    }
    public boolean contains(T value) {
        int check_key = value.hashCode();
        head.lock.lock();
        Node predecessor = head;
        try {
            Node  current = predecessor.next;
            current.lock.lock();
            //Traverse down the sorted list till you find a key equal to or greater than your own
            try {
                while(current.key < check_key){
                    predecessor.lock.unlock();
                    predecessor = current;
                    current = current.next;
                    current.lock.lock();
                }
                //If you find the key return true
                return (current.key == check_key);
//                    return true;
//                // Else no such key exists
//                else
//                    return false;
            } finally {
                current.lock.unlock();
            }
        } finally{
            predecessor.lock.unlock();
        }
    }
    private class Node<T>{
        final T value;
        volatile int key;
        volatile Node<T> next;
        Node(T value){
            this.value= value;
        }
        private ReentrantLock lock = new ReentrantLock();
    }
}
