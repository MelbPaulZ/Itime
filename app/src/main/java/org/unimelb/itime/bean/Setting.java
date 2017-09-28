package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by yinchuandong on 11/1/17.
 */

@Entity
public class Setting implements Serializable {

    private static final long serialVersionUID = -2510305145153762978L;
    @Id
    private String userUid = "";
    private boolean enableNotification =  true;
    private boolean enableEventAlert = true;
    private boolean enablePreviewText = true;
    private boolean systemSound = true;
    private int defaultSoloAlertTime = 0;
    private int defaultGroupAlertTime = 0;
    private int defaultAllDayAlertTime = 0;
    private boolean showPreviewText = true;
    private boolean appAlertSound = true;
    private boolean systemVibrate = true;
    private boolean enableFriendRequestEmail = true;
    private boolean enableEventInvitationEmail = true;
    private boolean enableEventConfirmEmail = true;

    @Generated(hash = 857170732)
    public Setting(String userUid, boolean enableNotification, boolean enableEventAlert, boolean enablePreviewText, boolean systemSound, int defaultSoloAlertTime, int defaultGroupAlertTime, int defaultAllDayAlertTime, boolean showPreviewText, boolean appAlertSound, boolean systemVibrate, boolean enableFriendRequestEmail, boolean enableEventInvitationEmail, boolean enableEventConfirmEmail) {
        this.userUid = userUid;
        this.enableNotification = enableNotification;
        this.enableEventAlert = enableEventAlert;
        this.enablePreviewText = enablePreviewText;
        this.systemSound = systemSound;
        this.defaultSoloAlertTime = defaultSoloAlertTime;
        this.defaultGroupAlertTime = defaultGroupAlertTime;
        this.defaultAllDayAlertTime = defaultAllDayAlertTime;
        this.showPreviewText = showPreviewText;
        this.appAlertSound = appAlertSound;
        this.systemVibrate = systemVibrate;
        this.enableFriendRequestEmail = enableFriendRequestEmail;
        this.enableEventInvitationEmail = enableEventInvitationEmail;
        this.enableEventConfirmEmail = enableEventConfirmEmail;
    }




    @Generated(hash = 909716735)
    public Setting() {
    }

    public boolean isEnableNotification() {
        return enableNotification;
    }

    public void setEnableNotification(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    public boolean isShowPreviewText() {
        return showPreviewText;
    }

    public void setShowPreviewText(boolean showPreviewText) {
        this.showPreviewText = showPreviewText;
    }

    public boolean isAppAlertSound() {
        return appAlertSound;
    }

    public void setAppAlertSound(boolean appAlertSound) {
        this.appAlertSound = appAlertSound;
    }

    public boolean isSystemVibrate() {
        return systemVibrate;
    }

    public void setSystemVibrate(boolean systemVibrate) {
        this.systemVibrate = systemVibrate;
    }

    public boolean isEnableFriendRequestEmail() {
        return enableFriendRequestEmail;
    }

    public void setEnableFriendRequestEmail(boolean enableFriendRequestEmail) {
        this.enableFriendRequestEmail = enableFriendRequestEmail;
    }

    public boolean isEnableEventInvitationEmail() {
        return enableEventInvitationEmail;
    }

    public void setEnableEventInvitationEmail(boolean enableEventInvitationEmail) {
        this.enableEventInvitationEmail = enableEventInvitationEmail;
    }

    public boolean isEnableEventConfirmEmail() {
        return enableEventConfirmEmail;
    }

    public void setEnableEventConfirmEmail(boolean enableEventConfirmEmail) {
        this.enableEventConfirmEmail = enableEventConfirmEmail;
    }

    public boolean getEnableEventConfirmEmail() {
        return this.enableEventConfirmEmail;
    }

    public boolean getEnableEventInvitationEmail() {
        return this.enableEventInvitationEmail;
    }

    public boolean getEnableFriendRequestEmail() {
        return this.enableFriendRequestEmail;
    }

    public boolean getSystemVibrate() {
        return this.systemVibrate;
    }

    public boolean getAppAlertSound() {
        return this.appAlertSound;
    }

    public boolean getShowPreviewText() {
        return this.showPreviewText;
    }

    public boolean getEnableNotification() {
        return this.enableNotification;
    }

    public int getDefaultAlertTime() {
        return this.defaultGroupAlertTime;
    }

    public void setDefaultAlertTime(int defaultAlertTime) {
        this.defaultGroupAlertTime = defaultAlertTime;
    }

    public String getUserUid() {
        return this.userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }




    public boolean getEnableEventAlert() {
        return this.enableEventAlert;
    }




    public void setEnableEventAlert(boolean enableEventAlert) {
        this.enableEventAlert = enableEventAlert;
    }




    public boolean getEnablePreviewText() {
        return this.enablePreviewText;
    }




    public void setEnablePreviewText(boolean enablePreviewText) {
        this.enablePreviewText = enablePreviewText;
    }




    public boolean getSystemSound() {
        return this.systemSound;
    }




    public void setSystemSound(boolean systemSound) {
        this.systemSound = systemSound;
    }




    public int getDefaultSoloAlertTime() {
        return this.defaultSoloAlertTime;
    }




    public void setDefaultSoloAlertTime(int defaultSoloAlertTime) {
        this.defaultSoloAlertTime = defaultSoloAlertTime;
    }




    public int getDefaultGroupAlertTime() {
        return this.defaultGroupAlertTime;
    }




    public void setDefaultGroupAlertTime(int defaultGroupAlertTime) {
        this.defaultGroupAlertTime = defaultGroupAlertTime;
    }




    public int getDefaultAllDayAlertTime() {
        return this.defaultAllDayAlertTime;
    }




    public void setDefaultAllDayAlertTime(int defaultAllDayAlertTime) {
        this.defaultAllDayAlertTime = defaultAllDayAlertTime;
    }
}
