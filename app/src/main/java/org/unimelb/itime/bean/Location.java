package org.unimelb.itime.bean;


import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 20/6/17.
 */
@Entity
public class Location implements Serializable{
    private static final long serialVersionUID = 955475191468036448L;
    
    private String locationString1 = "";
    private String locationString2 = "";
    private String locationNote = "";
    private String locationLatitude = "";
    private String locationLongitude = "";



    @Generated(hash = 1765972794)
    public Location(String locationString1, String locationString2,
            String locationNote, String locationLatitude,
            String locationLongitude) {
        this.locationString1 = locationString1;
        this.locationString2 = locationString2;
        this.locationNote = locationNote;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    @Generated(hash = 375979639)
    public Location() {
    }

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

    public String getLocationNote() {
        return this.locationNote;
    }

    public void setLocationNote(String locationNote) {
        this.locationNote = locationNote;
    }

    public String getLocationLatitude() {
        return this.locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return this.locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }


}
