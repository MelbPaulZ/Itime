package org.unimelb.itime.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 4/7/17.
 */

@Entity
public class MessageGroup implements Serializable{

    public final static int TYPE_EVENT_MESSAGE_GROUP = 1;
    public final static int SYSTEM_MESSAGE_GROUP = 2;
    public final static int CHAT_MESSAGE = 3;
    private static final long serialVersionUID = 5348488221692832920L;

    private int type; // 1 -> event message group   2 -> system message group  3 -> chat message
    private String title = "";
    private String status = "";
    private int messageGroupUid;
    private int userUid;
    private String eventUid = "";
    private boolean isMute;
    private int deleteLevel;
    private String createAt = "";
    private String updateAt = "";
    private boolean isArchived = false;

    @Convert(converter = MessageConverter.class, columnType = String.class)
    private List<Message> message;

    @Generated(hash = 1832732690)
    public MessageGroup(int type, String title, String status, int messageGroupUid, int userUid,
            String eventUid, boolean isMute, int deleteLevel, String createAt, String updateAt,
            boolean isArchived, List<Message> message) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.messageGroupUid = messageGroupUid;
        this.userUid = userUid;
        this.eventUid = eventUid;
        this.isMute = isMute;
        this.deleteLevel = deleteLevel;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isArchived = isArchived;
        this.message = message;
    }

    @Generated(hash = 1950961560)
    public MessageGroup() {
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public boolean getIsMute() {
        return this.isMute;
    }

    public void setIsMute(boolean isMute) {
        this.isMute = isMute;
    }

    public boolean getIsArchived() {
        return this.isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public static class MessageConverter implements PropertyConverter<List<Message>, String> {
        Gson gson = new Gson();
        @Override
        public List<Message> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<Message>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<Message> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }
}
