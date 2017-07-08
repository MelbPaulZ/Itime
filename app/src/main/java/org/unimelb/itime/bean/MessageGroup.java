package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 4/7/17.
 */

public class MessageGroup implements Serializable{
    public final static int TYPE_EVENT_MESSAGE_GROUP = 1;
    public final static int SYSTEM_MESSAGE_GROUP = 2;
    public final static int CHAT_MESSAGE = 3;

    private int messageGroupUid;
    private int type; // 1 -> event message group   2 -> system message group  3 -> chat message
    private int userUid;
    private String eventUid = "";
    private String title = "";
    private boolean isMute;
    private int deleteLevel;
    private String createAt = "";
    private String updateAt = "";

    public int getMessageGroupUid() {
        return messageGroupUid;
    }

    public void setMessageGroupUid(int messageGroupUid) {
        this.messageGroupUid = messageGroupUid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
