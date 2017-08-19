package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Qiushuo Huang on 2016/12/21.
 */

@Entity
public class FriendRequest implements ITimeUserInfoInterface, Serializable {

    public static final String STATUS_SENT = "sent";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String SOURCE_ITIME = "itime";
    public static final String SOURCE_MOBILE = "mobile";
    public static final String SOURCE_GMAIL = "gmail";
    public static final String SOURCE_FACEBOOK = "facebook";
    public static final String DISPLAY_STATUS_ADDED = "Added";
    public static final String DISPLAY_STATUS_ACCEPT = "Accept";
    public static final String DISPLAY_STATUS_ACCEPTED = "Accepted";
    private static final long serialVersionUID = 2948528451051314761L;


    private String source;
    private String note;
    private String status;
    @Id
    private String freqUid;

    //sender uid
    private String userUid;

    //receiver uid
    private String freqUserUid;
    private String userId;
    private int isRead;
    private String createdAt;
    private String updatedAt;
    private String displayStatus;
    private int commonContact = 0;
    private boolean sender = false;

    @Convert(converter = User.UserConverter.class , columnType = String.class)
    private User userDetail;

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public User getUser(){
        return userDetail;
    }


    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    @Generated(hash = 293944008)
    public FriendRequest(String source, String note, String status, String freqUid, String userUid, String freqUserUid, String userId, int isRead, String createdAt, String updatedAt, String displayStatus,
            int commonContact, boolean sender, User userDetail) {
        this.source = source;
        this.note = note;
        this.status = status;
        this.freqUid = freqUid;
        this.userUid = userUid;
        this.freqUserUid = freqUserUid;
        this.userId = userId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.displayStatus = displayStatus;
        this.commonContact = commonContact;
        this.sender = sender;
        this.userDetail = userDetail;
    }

    public FriendRequest(){}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFreqUid() {
        return freqUid;
    }

    public void setFreqUid(String freqUid) {
        this.freqUid = freqUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getFreqUserUid() {
        return freqUserUid;
    }

    public void setFreqUserUid(String freqUserUid) {
        this.freqUserUid = freqUserUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return isRead>0;
    }

    public void setRead(boolean read) {
       if(read){
           isRead =1;
       }else{
           isRead = 0;
       }
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

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }


    @Override
    public String getShowPhoto() {
        return userDetail.getShowPhoto();
    }

    @Override
    public String getShowName() {
        return userDetail.getShowName();
    }

    @Override
    public String getSecondInfo() {
        return userDetail.getUserId();
    }

    public int getCommonContact() {
        return commonContact;
    }

    public void setCommonContact(int commonContact) {
        this.commonContact = commonContact;
    }

    public boolean isSender() {
        return sender;
    }

    public void setSender(boolean sender) {
        this.sender = sender;
    }

    public boolean getSender() {
        return this.sender;
    }
}
