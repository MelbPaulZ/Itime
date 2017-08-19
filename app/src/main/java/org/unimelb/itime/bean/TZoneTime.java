package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 22/6/17.
 */

public class TZoneTime implements Serializable, Cloneable{
    private String dateTime = "";
    private String timeZone = "";
    private String date = "";

    public TZoneTime() {
    }

    public TZoneTime(String dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    protected TZoneTime clone() {
        TZoneTime tZoneTime = null;
        try {
            tZoneTime = (TZoneTime) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tZoneTime;
    }
}
