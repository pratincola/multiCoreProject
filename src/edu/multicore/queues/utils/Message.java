package edu.multicore.queues.utils;

/**
 * Created by pratik1 on 7/27/15.
 */
public class Message {

    private int id;
    private String message;

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{ id:" + id + "}";
    }
}
