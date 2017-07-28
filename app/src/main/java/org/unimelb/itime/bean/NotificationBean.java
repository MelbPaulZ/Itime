package org.unimelb.itime.bean;

/**
 * Created by Qiushuo Huang on 2017/2/11.
 */

public class NotificationBean {
    //"消息内容"
    private String alert = "";

    //"显示在通知栏的标题"
    private String title = "";

    //"com.your_company.push"
    private String action = "";

    //true/false // 用于控制是否关闭推送通知栏提醒，默认为 false，即不关闭通知栏提醒
    private boolean silent = false;

    private boolean appAlertSound = true;

    private boolean systemVibrate = true;

    public boolean isSystemVibrate() {
        return systemVibrate;
    }

    public void setSystemVibrate(boolean systemVibrate) {
        this.systemVibrate = systemVibrate;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isAppAlertSound() {
        return appAlertSound;
    }

    public void setAppAlertSound(boolean appAlertSound) {
        this.appAlertSound = appAlertSound;
    }
}
