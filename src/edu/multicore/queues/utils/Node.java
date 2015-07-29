package edu.multicore.queues.utils;

/**
 * Created by pratik1 on 7/27/15.
 */
public class Node<T> {

    public T value;
    public Node<T> next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }

}
