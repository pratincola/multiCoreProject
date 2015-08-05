package hw4.q2;

import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedListSet<T> implements ListSet<T> {
    private Node head;
    private ReentrantLock lock = new ReentrantLock();

    public CoarseGrainedListSet(){
        head = new Node<>(Integer.MIN_VALUE);
        head.next= new Node<>(Integer.MAX_VALUE);
    }
    public boolean add(T value) {
        Node predecessor, current;
        int add_key = value.hashCode();
        lock.lock();
        try  {
            //Every thread's local predecessor and current nodes
            predecessor = head;
            current = predecessor.next;
            //Traverse down the sorted list till you find a key equal to or greater than your own
            while (current.key < add_key){
                predecessor = current;
                current = current.next;
            }
            //If the key is equal you are trying to add a duplicate
            if (add_key == current.key){
                return false;
            }
            //Else add the element to the list
            else{
                Node new_addition = new Node<>(value);
                new_addition.next = current;
                predecessor.next = new_addition;
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(T value) {
        Node predecessor, current;
        int rem_key = value.hashCode();
        lock.lock();
        try {
            predecessor = head;
            current = predecessor.next;
            //Traverse down the sorted list till you find a key equal to or greater than your own
            while(current.key < rem_key){
                predecessor = current;
                current = current.next;
            }
            //If you find the key remove it
            if (current.key == rem_key) {
                predecessor.next = current.next;
                return true;
            }
            //Else no such key exists
            else
                return false;
        } finally{
            lock.unlock();
        }
    }

    public boolean contains(T value) {
        Node predecessor, current;
        int check_key = value.hashCode();
        lock.lock();
        try {
            predecessor = head;
            current = predecessor.next;
            //Traverse down the sorted list till you find a key equal to or greater than your own
            while(current.key < check_key){
                predecessor = current;
                current = current.next;
            }
            //If you find the key return true
            return (current.key == check_key);
        } finally{
            lock.unlock();
        }
    }

    private class Node<T>{
        final T value;
        volatile int key;
        volatile Node<T> next;
        Node(T value){
            this.value= value;
            this.key = value.hashCode();
        }
        Node(int k){
            this.value = null;
            this.key = k;
        }
    }
}

