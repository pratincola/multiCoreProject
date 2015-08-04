package edu.multicore.queues.utils;

/**
 * Created by jtharp on 8/3/2015.
 */
public class Settings {


    private static Settings obj;
    private boolean log;
    private boolean rr = false;

    public static  synchronized Settings getInstance(){
        if(obj == null){
            obj = new Settings();
        }
        return obj;
    }

    public void setRr(boolean rr) {
        this.rr = rr;
    }

    public boolean isRr() {
        return rr;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }
}
