package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 30/9/17.
 */

@Entity
public class RecentLocation implements Serializable{
    
    private static final long serialVersionUID = -3471516883997291384L;
    @Id
    private String primaryString;
    private String secondaryString;
    private String userUid;
    private long selectDate;

    @Generated(hash = 1634898461)
    public RecentLocation(String primaryString, String secondaryString,
            String userUid, long selectDate) {
        this.primaryString = primaryString;
        this.secondaryString = secondaryString;
        this.userUid = userUid;
        this.selectDate = selectDate;
    }

    @Generated(hash = 1954311721)
    public RecentLocation() {
    }

    public String getPrimaryString() {
        return primaryString;
    }

    public void setPrimaryString(String primaryString) {
        this.primaryString = primaryString;
    }

    public String getSecondaryString() {
        return secondaryString;
    }

    public void setSecondaryString(String secondaryString) {
        this.secondaryString = secondaryString;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public long getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(long selectDate) {
        this.selectDate = selectDate;
    }
}
