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
import org.greenrobot.greendao.DaoException;

/**
 * Created by Paul on 4/7/17.
 */

@Entity(active = true)
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

    @Id
    private String eventUid = "";
    private boolean isMute;
    private int deleteLevel;
    private String createdAt = "";
    private String updatedAt = "";
    private boolean isArchived = false;

    @Convert(converter = MessageConverter.class, columnType = String.class)
    private List<Message> message;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 745534098)
    private transient MessageGroupDao myDao;


    @Generated(hash = 1950961560)
    public MessageGroup() {
    }

    @Generated(hash = 39062078)
    public MessageGroup(int type, String title, String status, int messageGroupUid, int userUid,
            String eventUid, boolean isMute, int deleteLevel, String createdAt, String updatedAt,
            boolean isArchived, List<Message> message) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.messageGroupUid = messageGroupUid;
        this.userUid = userUid;
        this.eventUid = eventUid;
        this.isMute = isMute;
        this.deleteLevel = deleteLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isArchived = isArchived;
        this.message = message;
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

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1247233291)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageGroupDao() : null;
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
