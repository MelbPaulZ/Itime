package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 22/6/17.
 */

public class TZoneTime implements Serializable{
    private String dateTime = "";
    private String timeZone = "";

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
