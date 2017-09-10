package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 9/9/17.
 */

public class RecommandTime implements Serializable {
    private String dateTime = "";
    private String date = "";
    private String timeZone = "";

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
