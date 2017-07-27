package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 27/08/2016.
 */
public class MessageLocation {
    public int tag;
    public final String locationString;
    public final static int SUCCESS = 1;
    public final static int FAIL = -1;

    public MessageLocation(int tag,String locationString) {
        this.tag = tag;
        this.locationString = locationString;
    }
}
