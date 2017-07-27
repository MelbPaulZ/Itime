package org.unimelb.itime.restfulresponse;

/**
 * Created by Paul on 1/3/17.
 */

public class WebSocketRes {
    public static final String SYNC_USER = "sync_user";
    public static final String SYNC_CONTACT= "sync_contact";
    public static final String SYNC_EVENT = "sync_event";
    public static final String SYNC_INBOX = "sync_inbox";
    public static final String SYNC_SETTING = "sync_setting";
    public static final String SYNC_CALENDAR = "sync_calendar";
    public static final String SYNC_FRIEND_REQUEST = "sync_friend_request";
    private String type;

    public WebSocketRes() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

