package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 16/09/2016.
 */
public class PhotoUrl implements Cloneable,Serializable {

    private String url = "";
    private String photoUid = "";
    private String eventUid = "";
    private String filename = "";
    private String localPath = "";
    private boolean success;

    public PhotoUrl(String url){
        this.url = url;
    }

    public PhotoUrl(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoUid() {
        return photoUid;
    }

    public void setPhotoUid(String photoUid) {
        this.photoUid = photoUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public PhotoUrl clone() {
        PhotoUrl photoUrl = null;
        try
        {
            photoUrl = (PhotoUrl) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return photoUrl;
    }
}
