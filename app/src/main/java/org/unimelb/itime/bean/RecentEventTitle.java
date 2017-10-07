package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 7/10/17.
 */
@Entity
public class RecentEventTitle implements Serializable{


    private static final long serialVersionUID = -6377842481694208992L;

    @Index
    private String title, userUid;
    private long time;


    @Generated(hash = 1395882807)
    public RecentEventTitle(String title, String userUid, long time) {
        this.title = title;
        this.userUid = userUid;
        this.time = time;
    }

    @Generated(hash = 1981263399)
    public RecentEventTitle() {
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
