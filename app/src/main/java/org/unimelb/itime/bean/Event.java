package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 6/6/17.
 */

public class Event implements Serializable{
    private String title="";
    private String note="";
    private String url="";
    private String location="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
