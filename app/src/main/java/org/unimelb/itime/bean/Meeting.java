package org.unimelb.itime.bean;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.DaoException;

import java.io.Serializable;

/**
 * Created by yuhaoliu on 26/06/2017.
 */
@Entity(active = true)
public class Meeting implements Comparable<Meeting>, Serializable{
    private static final long serialVersionUID = 1536918684956999598L;

    @Id
    private String meetingUid = "";
    private String eventUid = "";
    private String hostUserUid = "";
    private String userUid = "";
    private String info = "Hanna Baker invite you to ";
    private String avatar = "";
    private String createdAt = "";
    private String updatedAt = "";

    @Convert(converter = Meeting.UpdateFieldConverter.class , columnType = String.class)
    private String[] updateField = new String[]{};

    @Convert(converter = Meeting.EventConverter.class , columnType = String.class)
    private Event event;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1797444500)
    private transient MeetingDao myDao;

    @Generated(hash = 1639379472)
    public Meeting(String meetingUid, String eventUid, String hostUserUid, String userUid,
            String info, String avatar, String createdAt, String updatedAt,
            String[] updateField, Event event) {
        this.meetingUid = meetingUid;
        this.eventUid = eventUid;
        this.hostUserUid = hostUserUid;
        this.userUid = userUid;
        this.info = info;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updateField = updateField;
        this.event = event;
    }

    @Generated(hash = 171861101)
    public Meeting() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int compareTo(@NonNull Meeting o) {
        return event.compareTo(o.getEvent());
    }

    public String getMeetingUid() {
        return this.meetingUid;
    }

    public void setMeetingUid(String meetingUid) {
        this.meetingUid = meetingUid;
    }

    public String getEventUid() {
        return this.eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getUserUid() {
        return this.userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String[] getUpdateField() {
        return this.updateField;
    }

    public void setUpdateField(String[] updateField) {
        this.updateField = updateField;
    }

    public String getHostuseruid() {
        return this.hostUserUid;
    }

    public void setHostuseruid(String hostuseruid) {
        this.hostUserUid = hostuseruid;
    }

    public String getHostUserUid() {
        return this.hostUserUid;
    }

    public void setHostUserUid(String hostUserUid) {
        this.hostUserUid = hostUserUid;
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
    @Generated(hash = 1584316095)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMeetingDao() : null;
    }

    /************ Converter
     *
     */

    public static class EventConverter implements PropertyConverter<Event , String> {
        Gson gson = new Gson();
        @Override
        public Event convertToEntityProperty(String databaseValue) {
            return gson.fromJson(databaseValue, Event.class);
        }

        @Override
        public String convertToDatabaseValue(Event entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class UpdateFieldConverter implements PropertyConverter<String[] , String> {
        Gson gson = new Gson();

        @Override
        public String[] convertToEntityProperty(String databaseValue) {
            return gson.fromJson(databaseValue, String[].class);
        }

        @Override
        public String convertToDatabaseValue(String[] entityProperty) {
            return gson.toJson(entityProperty);
        }
    }
}
