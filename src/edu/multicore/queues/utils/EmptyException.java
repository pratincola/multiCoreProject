package edu.multicore.queues.utils;

/**
 * Created by pratik1 on 7/27/15.
 */
public class EmptyException extends Exception {

    public EmptyException(String message){
        super(message);
    }
    public EmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
