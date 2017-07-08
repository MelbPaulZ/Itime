package org.unimelb.itime.bean;

import android.support.annotation.NonNull;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class Meeting implements Comparable<Meeting>{
    Event event;
    String sysMsg = "Hanna Baker invite you to ";

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getSysMsg() {
        return sysMsg;
    }

    public void setSysMsg(String sysMsg) {
        this.sysMsg = sysMsg;
    }

    @Override
    public int compareTo(@NonNull Meeting o) {
        return event.compareTo(o.getEvent());
    }
}
