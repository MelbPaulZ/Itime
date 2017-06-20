package org.unimelb.itime.bean;


import java.io.Serializable;

/**
 * Created by Paul on 20/6/17.
 */
public class Location implements Serializable{

    private String locationString1 = "";
    private String locationString2 = "";

    public String getLocationString1() {
        return locationString1;
    }

    public void setLocationString1(String locationString1) {
        this.locationString1 = locationString1;
    }

    public String getLocationString2() {
        return locationString2;
    }

    public void setLocationString2(String locationString2) {
        this.locationString2 = locationString2;
    }
}
