package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Qiushuo Huang on 2017/3/17.
 */

@Entity
public class Account {
    public static final String SOURCE_GOOGLE = "google";
    public static final String SOURCE_ITIME = "itime";
    public static final String SOURCE_UNIMELB = "unimelb";
    public static final String SOURCE_MONASH= "monash";

    private String userUid = "";
    @Id
    private String accountUid = "";
    private String accountId = "";
    private String name = "";
    private String source = "";
    private String extra = "";
    private int deleteLevel = 0;
    private String createdAt = "";
    private String updatedAt = "";

    @Generated(hash = 1137460671)
    public Account(String userUid, String accountUid, String accountId, String name, String source, String extra, int deleteLevel, String createdAt, String updatedAt) {
        this.userUid = userUid;
        this.accountUid = accountUid;
        this.accountId = accountId;
        this.name = name;
        this.source = source;
        this.extra = extra;
        this.deleteLevel = deleteLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 882125521)
    public Account() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getAccountUid() {
        return accountUid;
    }

    public void setAccountUid(String accountUid) {
        this.accountUid = accountUid;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
}
