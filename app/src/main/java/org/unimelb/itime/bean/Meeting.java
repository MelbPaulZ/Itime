package org.unimelb.itime.bean;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class Meeting {
    Event event;
    String sysMsg = "fuck u all";

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
}
