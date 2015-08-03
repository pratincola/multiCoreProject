package edu.multicore.queues.utils;

/**
 * Created by jtharp on 8/3/2015.
 */
public class Settings {


    private static Settings obj;
    private boolean log;

    public static  synchronized Settings getInstance(){
        if(obj == null){
            obj = new Settings();
        }
        return obj;
    }


    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }
}
