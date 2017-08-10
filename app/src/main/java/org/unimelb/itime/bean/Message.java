package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 4/7/17.
 */

public class Message implements Serializable {


    private static final long serialVersionUID = 6568872098666058601L;
    private String photo = "";
    private String title = "";
    private String subtitle1 = "";
    private int type; // 0 -> default?

    private int messageUid;
    private int messageGroupUid;
    private int userUid;
    private String eventUid = "";
    private String displayName = "";
    private boolean isRead;
    private int deleteLevel;
    private String createdAt = "";
    private String updatedAt = "";


    public int getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(int messageUid) {
        this.messageUid = messageUid;
    }

    public int getMessageGroupUid() {
        return messageGroupUid;
    }

    public void setMessageGroupUid(int messageGroupUid) {
        this.messageGroupUid = messageGroupUid;
    }

    public int getUserUid() {
        return userUid;
    }

    public void setUserUid(int userUid) {
        this.userUid = userUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle1() {
        return subtitle1;
    }

    public void setSubtitle1(String subtitle1) {
        this.subtitle1 = subtitle1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}
