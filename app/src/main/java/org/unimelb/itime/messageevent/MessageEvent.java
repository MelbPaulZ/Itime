package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 25/08/2016.
 */
public class MessageEvent {
    public final static int RELOAD_EVENT = 1;
    public final static int LOGOUT = 2;
    public final static int LOGOUTWITHDB = 3;
    public final static int LOGIN_SUCCESS = 4;
    public final static int LOGIN_REGISTER_LEANCLOUD =5;
    public final static int RELOAD_MEETING = 6;
    public final static int RELOAD_ITIME_ACTIVITIES = 7;

    public String message;
    public int task;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int task){
        this.task = task;
    }


}
