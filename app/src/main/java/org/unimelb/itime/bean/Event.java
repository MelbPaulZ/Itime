package org.unimelb.itime.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleInterface;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.listeners.ITimeInviteeInterface;

import org.greenrobot.greendao.annotation.Generated;
import org.unimelb.itime.util.rulefactory.RuleModel;

/**
 * Created by Paul on 6/6/17.
 */

@Entity
public class Event implements ITimeEventInterface<Event>, Serializable, Cloneable, RuleInterface, ITimeComparable<Event> {

    private static final long serialVersionUID = -7635944932445335914L;

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_UPDATING = "updating";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_CANCELLED = "cancelled";

    public static final String TYPE_GROUP = "group";
    public static final String TYPE_SOLO = "solo";

    public static final int CAN_SEE_EACH_OTHER = 1;
    public static final int CANNOT_SEE_EACH_OTHER = 0;


    // new event attributes
    private String id = "";

    @Convert(converter = RecurrenceConverter.class, columnType = String.class)
    private String[] recurrence = {};

    private String status = "";
    private String summary = "";
    private String description = "";
    private String url = "";
    @Convert(converter = Event.LocationConverter.class, columnType = String.class)
    private Location location = new Location();
    private int reminder = -1; // mins
    private String source = "";
    @Id
    private String eventUid = "";
    private String calendarUid = "";
    private String recurringEventUid = "";
    private String host = "";
    private String self = "";
    private String hostUserUid = "";
    private String userUid = "";
    private String eventType = "";
    private int inviteeVisibility;
    private int freebusyAccess;
    private int showLevel;
    private int deleteLevel;
    private String createdAt= "";
    private String updatedAt = "";
    @Convert(converter = Event.PhotoUrlConverter.class , columnType = String.class)
    private List<PhotoUrl> photos = new ArrayList<>();
    private boolean isAllDay;
    private String extra;
    @Convert(converter = Event.TZoneTimeConverter.class, columnType = String.class)
    private TZoneTime start = new TZoneTime();
    @Convert(converter = Event.TZoneTimeConverter.class, columnType = String.class)
    private TZoneTime end = new TZoneTime();

    @Convert(converter = Event.InviteeConverter.class, columnType = String.class)
    private Map<String, Invitee> invitee = new HashMap<>();

    @Convert(converter = Event.TimeslotConverter.class , columnType = String.class)
    private Map<String, TimeSlot> timeslot = new HashMap<>();
    @Convert(converter = Event.TimeslotInviteeConverter.class, columnType = String.class)
    private Map<String, TimeslotInvitee> timeslotInvitee = new HashMap<>();

    private transient RuleModel rule = new RuleModel();
    private String note = "";
    private String coverPhoto = "";
    private int duration;
    private String greeting = "";
    private boolean isArchived;
    private boolean isMute;
    private boolean isPinned;

    @Generated(hash = 344677835)
    public Event() {
    }




    @Generated(hash = 1646521494)
    public Event(String id, String[] recurrence, String status, String summary, String description, String url,
            Location location, int reminder, String source, String eventUid, String calendarUid, String recurringEventUid,
            String host, String self, String hostUserUid, String userUid, String eventType, int inviteeVisibility,
            int freebusyAccess, int showLevel, int deleteLevel, String createdAt, String updatedAt, List<PhotoUrl> photos,
            boolean isAllDay, String extra, TZoneTime start, TZoneTime end, Map<String, Invitee> invitee,
            Map<String, TimeSlot> timeslot, Map<String, TimeslotInvitee> timeslotInvitee, String note, String coverPhoto,
            int duration, String greeting, boolean isArchived, boolean isMute, boolean isPinned) {
        this.id = id;
        this.recurrence = recurrence;
        this.status = status;
        this.summary = summary;
        this.description = description;
        this.url = url;
        this.location = location;
        this.reminder = reminder;
        this.source = source;
        this.eventUid = eventUid;
        this.calendarUid = calendarUid;
        this.recurringEventUid = recurringEventUid;
        this.host = host;
        this.self = self;
        this.hostUserUid = hostUserUid;
        this.userUid = userUid;
        this.eventType = eventType;
        this.inviteeVisibility = inviteeVisibility;
        this.freebusyAccess = freebusyAccess;
        this.showLevel = showLevel;
        this.deleteLevel = deleteLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.photos = photos;
        this.isAllDay = isAllDay;
        this.extra = extra;
        this.start = start;
        this.end = end;
        this.invitee = invitee;
        this.timeslot = timeslot;
        this.timeslotInvitee = timeslotInvitee;
        this.note = note;
        this.coverPhoto = coverPhoto;
        this.duration = duration;
        this.greeting = greeting;
        this.isArchived = isArchived;
        this.isMute = isMute;
        this.isPinned = isPinned;
    }


    

    @Override
    public Event clone() {
        Event event = null;
        try
        {
            event = (Event) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String[] getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String[] recurrence) {
        this.recurrence = recurrence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    @Override
    public void setStartTime(long l) {
        String time = EventUtil.getFormatTimeString(l, EventUtil.TIME_ZONE_PATTERN);
        this.start.setDateTime(time);
    }

    @Override
    public long getStartTime() {
        long time = EventUtil.parseTimeZoneToDate(start.getDateTime()).getTime();
        return time;
    }

    @Override
    public void setEndTime(long l) {
        String time = EventUtil.getFormatTimeString(l, EventUtil.TIME_ZONE_PATTERN);
        this.end.setDateTime(time);
    }

    @Override
    public long getEndTime() {
        long time = EventUtil.parseTimeZoneToDate(end.getDateTime()).getTime();
        return time;
    }

    @Override
    public List<? extends ITimeInviteeInterface> getDisplayInvitee() {
        return null;
    }

    @Override
    public void setHighLighted(boolean b) {

    }

    @Override
    public boolean isHighlighted() {
        return false;
    }

    @Override
    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    @Override
    public boolean getIsAllDay() {
        return this.isAllDay;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getCalendarUid() {
        return calendarUid;
    }

    public void setCalendarUid(String calendarUid) {
        this.calendarUid = calendarUid;
    }

    public String getRecurringEventUid() {
        return recurringEventUid;
    }

    public void setRecurringEventUid(String recurringEventUid) {
        this.recurringEventUid = recurringEventUid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getHostUserUid() {
        return hostUserUid;
    }

    public void setHostUserUid(String hostUserUid) {
        this.hostUserUid = hostUserUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getEventType() {
        return eventType;
    }

    public boolean isConfirmed() {
        return status.equals(Event.STATUS_CONFIRMED);
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getInviteeVisibility() {
        return inviteeVisibility;
    }

    public void setInviteeVisibility(int inviteeVisibility) {
        this.inviteeVisibility = inviteeVisibility;
    }

    public int getFreebusyAccess() {
        return freebusyAccess;
    }

    public void setFreebusyAccess(int freebusyAccess) {
        this.freebusyAccess = freebusyAccess;
    }

    public int getShowLevel() {
        return showLevel;
    }

    public void setShowLevel(int showLevel) {
        this.showLevel = showLevel;
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

    public List<PhotoUrl> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoUrl> photos) {
        this.photos = photos;
    }

    @Override
    public int isShownInCalendar() {
        return 0;
    }

    @Override
    public String getLocationName() {
        return location!=null? location.getLocationString1() : "";
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


    public Map<String, TimeslotInvitee> getTimeslotInvitee() {
        return timeslotInvitee;
    }

    public void setTimeslotInvitee(Map<String,TimeslotInvitee> timeslotInvitee) {
        this.timeslotInvitee = timeslotInvitee;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(Event event) {
        long selfStartTime = this.getStartTime();
        long cmpTgtStartTime = event.getStartTime();
        int result = selfStartTime < cmpTgtStartTime ? -1 : 1;

        if (result == -1){
            return result;
        }else {
            return selfStartTime == cmpTgtStartTime ? 0 : 1;
        }
    }


    @Override
    public boolean iTimeEquals(Event obj2) {
        return this.getEventUid().equals(obj2.getEventUid());
    }

    public TZoneTime getStart() {
        return this.start;
    }

    public void setStart(TZoneTime start) {
        this.start = start;
    }

    public TZoneTime getEnd() {
        return this.end;
    }

    public void setEnd(TZoneTime end) {
        this.end = end;
    }


    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // add methods
    public long getDurationMilliseconds(){
        return getStartTime() - getEndTime();
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public boolean getArchive() {
        return this.isArchived;
    }

    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        this.isMute = mute;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        this.isPinned = pinned;
    }

    public boolean getMute() {
        return this.isMute;
    }

    public boolean getPin() {
        return this.isPinned;
    }

    public Map<String, Invitee> getInvitee() {
        return this.invitee;
    }

    public void setInvitee(Map<String, Invitee> invitee) {
        this.invitee = invitee;
    }


    public Map<String, TimeSlot> getTimeslot() {
        return this.timeslot;
    }


    public void setTimeslot(Map<String, TimeSlot> timeslot) {
        this.timeslot = timeslot;
    }




    public boolean getIsArchived() {
        return this.isArchived;
    }




    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }




    public boolean getIsMute() {
        return this.isMute;
    }




    public void setIsMute(boolean isMute) {
        this.isMute = isMute;
    }




    public boolean getIsPinned() {
        return this.isPinned;
    }




    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }



    public static class TimeslotConverter implements PropertyConverter<Map<String, TimeSlot> , String> {
        Gson gson = new Gson();
        @Override
        public Map<String, TimeSlot> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<Map<String, TimeSlot>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(Map<String, TimeSlot> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class PhotoUrlConverter implements PropertyConverter<List<PhotoUrl> , String>{
        Gson gson = new Gson();
        @Override
        public List<PhotoUrl> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<PhotoUrl>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<PhotoUrl> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class InviteeConverter implements PropertyConverter<Map<String, Invitee> , String>{
        Gson gson = new Gson();
        @Override
        public Map<String, Invitee> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<Map<String, Invitee>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(Map<String, Invitee> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class LocationConverter implements PropertyConverter<Location, String>{
        Gson gson = new Gson();
        @Override
        public Location convertToEntityProperty(String databaseValue) {
            Location location = gson.fromJson(databaseValue, Location.class);
            return location;
        }

        @Override
        public String convertToDatabaseValue(Location entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class TZoneTimeConverter implements PropertyConverter<TZoneTime, String>{
        Gson gson = new Gson();
        @Override
        public TZoneTime convertToEntityProperty(String databaseValue) {
            TZoneTime tZoneTime = gson.fromJson(databaseValue, TZoneTime.class);
            return tZoneTime;
        }

        @Override
        public String convertToDatabaseValue(TZoneTime entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class RecurrenceConverter implements PropertyConverter<String[], String>{
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

    public static class TimeslotInviteeConverter implements PropertyConverter<Map<String, TimeslotInvitee>, String>{
        Gson gson = new Gson();

        @Override
        public Map<String, TimeslotInvitee> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<Map<String, TimeslotInvitee>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(Map<String, TimeslotInvitee> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }
}
