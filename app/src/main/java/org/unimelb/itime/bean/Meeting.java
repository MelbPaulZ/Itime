package org.unimelb.itime.bean;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by yuhaoliu on 26/06/2017.
 */
@Entity
public class Meeting implements Comparable<Meeting>{
    private String meetingUid = "";
    private String eventUid = "";
    private String userUid = "";
    private String info = "Hanna Baker invite you to ";
    private String avatar = "";
    private String createdAt = "";
    private String updatedAt = "";


    @Convert(converter = Meeting.EventConverter.class , columnType = String.class)
    private Event event;

    @Generated(hash = 171861101)
    public Meeting() {
    }

    @Generated(hash = 269515136)
    public Meeting(String meetingUid, String eventUid, String userUid, String info,
            String avatar, String createdAt, String updatedAt, Event event) {
        this.meetingUid = meetingUid;
        this.eventUid = eventUid;
        this.userUid = userUid;
        this.info = info;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
}
