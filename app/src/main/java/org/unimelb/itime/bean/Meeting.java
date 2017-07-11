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
    @Convert(converter = Meeting.EventConverter.class , columnType = String.class)
    private Event event;

    private String sysMsg = "Hanna Baker invite you to ";

    @Generated(hash = 1336096537)
    public Meeting(Event event, String sysMsg) {
        this.event = event;
        this.sysMsg = sysMsg;
    }

    @Generated(hash = 171861101)
    public Meeting() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getSysMsg() {
        return sysMsg;
    }

    public void setSysMsg(String sysMsg) {
        this.sysMsg = sysMsg;
    }

    @Override
    public int compareTo(@NonNull Meeting o) {
        return event.compareTo(o.getEvent());
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
