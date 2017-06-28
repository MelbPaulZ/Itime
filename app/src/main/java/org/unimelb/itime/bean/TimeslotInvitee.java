package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 28/6/17.
 */

public class TimeslotInvitee implements Serializable {
    private String status = "";
    private int rate;
    private String timeslotUid = "";
    private String inviteeUid = "";
    private String eventUid = "";
    private String userUid = "";


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTimeslotUid() {
        return timeslotUid;
    }

    public void setTimeslotUid(String timeslotUid) {
        this.timeslotUid = timeslotUid;
    }

    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
