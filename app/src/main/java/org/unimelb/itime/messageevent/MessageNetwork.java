package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 13/3/17.
 */

public class MessageNetwork {
    public final static int NETWORK_ERROR = 1001;
    public int task;
    public MessageNetwork(int task) {
        this.task = task;
    }

}
