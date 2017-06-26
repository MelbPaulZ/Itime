package org.unimelb.itime.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.unimelb.itime.util.rulefactory.RuleInterface;
import org.unimelb.itime.util.rulefactory.RuleModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.listeners.ITimeInviteeInterface;
import org.greenrobot.greendao.annotation.Generated;

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

    @Id
    private String eventUid = "";
    // for other calendars
    private String eventId;
    private String recurringEventUid = "";
    // for other calendars
    private String recurringEventId = "";
    private String calendarUid = "";
    private String iCalUID = "";
    private String hostUserUid = ""; // add by paul
    private String summary = "";
    private String url = "";
    @Convert(converter = Event.LocationConverter.class, columnType = String.class)
    private Location location = new Location();
    private String locationNote = "";
    private double locationLatitude;
    private double locationLongitude;
    private String note = "";
    private boolean isAllDay;
    private int showLevel;
    private String coverPhoto = "";
    private int reminder = -1; // mins
    private String greeting = "";
    private int duration = 0;

    @Convert(converter = RecurrenceConverter.class, columnType = String.class)
    private String[] recurrence = {};

    @Convert(converter = Event.InviteeConverter.class, columnType = String.class)
    private List<Invitee> invitees = new ArrayList<>();

    @Convert(converter = Event.PhotoUrlConverter.class , columnType = String.class)
    private List<PhotoUrl> photos = new ArrayList<>();

    @Convert(converter = Event.TimeslotConverter.class , columnType = String.class)
    private List<TimeSlot> timeslots = null;

    // later delete
    private transient long repeatEndsTime;
    private transient boolean isHost;
    private transient boolean highlighted;
    private int inviteeVisibility = 1; // default 1 = visible to each other, 0 = invisible

    public RuleModel getRule() {
        return rule;
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

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }

    @Expose(serialize = true, deserialize = true)
    private transient RuleModel rule = new RuleModel(this);

    @Property
    @NotNull
    private long startTime; // TODO: 22/6/17  change to starts
    @Property
    @NotNull
    private long endTime;
    @Property
    @NotNull
    private int eventType;
    @Property
    @NotNull
    private String display = "";

    @Convert(converter = Event.TZoneTimeConverter.class, columnType = String.class)
    private TZoneTime start = new TZoneTime();

    @Convert(converter = Event.TZoneTimeConverter.class, columnType = String.class)
    private TZoneTime end = new TZoneTime();

    public Event() {

    }

    @Generated(hash = 359872304)
    public Event(String eventUid, String eventId, String recurringEventUid, String recurringEventId, String calendarUid,
            String iCalUID, String hostUserUid, String summary, String url, Location location, String locationNote,
            double locationLatitude, double locationLongitude, String note, boolean isAllDay, int showLevel,
            String coverPhoto, int reminder, String greeting, int duration, String[] recurrence, List<Invitee> invitees,
            List<PhotoUrl> photos, List<TimeSlot> timeslots, int inviteeVisibility, long startTime, long endTime,
            int eventType, @NotNull String display, TZoneTime start, TZoneTime end) {
        this.eventUid = eventUid;
        this.eventId = eventId;
        this.recurringEventUid = recurringEventUid;
        this.recurringEventId = recurringEventId;
        this.calendarUid = calendarUid;
        this.iCalUID = iCalUID;
        this.hostUserUid = hostUserUid;
        this.summary = summary;
        this.url = url;
        this.location = location;
        this.locationNote = locationNote;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.note = note;
        this.isAllDay = isAllDay;
        this.showLevel = showLevel;
        this.coverPhoto = coverPhoto;
        this.reminder = reminder;
        this.greeting = greeting;
        this.duration = duration;
        this.recurrence = recurrence;
        this.invitees = invitees;
        this.photos = photos;
        this.timeslots = timeslots;
        this.inviteeVisibility = inviteeVisibility;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.display = display;
        this.start = start;
        this.end = end;
    }

    public int getInviteeVisibility() {
        return inviteeVisibility;
    }

    public void setInviteeVisibility(int inviteeVisibility) {
        this.inviteeVisibility = inviteeVisibility;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    @Override
    public void setTitle(String summary) {
        this.summary = summary;
    }

    @Override
    public String getTitle() {
        return this.summary;
    }

    @Override
    public void setHighLighted(boolean hightlighted) {
        this.highlighted = hightlighted;
    }

    @Override
    public boolean isHighlighted() {
        return this.highlighted;
    }

    @Override
    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    @Override
    public boolean isAllDay() {
        return this.isAllDay;
    }

    @Override
    public int isShownInCalendar() {
        return 0;
    }

    public void setEventId(String id){ this.eventUid = id;}

    public void setStartTime(long startTime){ this.startTime = startTime; }

    public void setEndTime(long endTime){ this.endTime = endTime; }

    public String getEventUid(){ return eventUid; }

    public long getStartTime(){return startTime;}

    public long getEndTime(){return endTime;}

    public void setDisplayEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getDisplayEventType() {
        return eventType;
    }

    public String getDisplayStatus() {
        return display;
    }

    public void setDisplayStatus(String display) {
        this.display = display;
    }

    public int getDuration(){
        return duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public long getDurationMilliseconds(){
        return (endTime - startTime);
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

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }


    @Override
    public List<? extends ITimeInviteeInterface> getDisplayInvitee() {
        return null;
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */

    public String getUrl() {
        return this.url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getSummary() {
        return this.summary;
    }


    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String[] getRecurrence() {
        return this.recurrence;
    }


    public void setRecurrence(String[] recurrence) {
        this.recurrence = recurrence;
    }


    public String getICalUID() {
        return this.iCalUID;
    }


    public void setICalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }


    public String getCalendarUid() {
        return this.calendarUid;
    }


    public void setCalendarUid(String calendarUid) {
        this.calendarUid = calendarUid;
    }


    public String getRecurringEventId() {
        return this.recurringEventId;
    }


    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }


    public String getRecurringEventUid() {
        return this.recurringEventUid;
    }


    public void setRecurringEventUid(String recurringEventUid) {
        this.recurringEventUid = recurringEventUid;
    }


    public String getEventId() {
        return this.eventId;
    }

    public String getiCalUID() {
        return iCalUID;
    }

    public void setiCalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }



    public String getLocationNote() {
        return locationNote;
    }

    public void setLocationNote(String locationNote) {
        this.locationNote = locationNote;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }


    public long getRepeatEndsTime() {
        return repeatEndsTime;
    }

    public void setRepeatEndsTime(long repeatEndsTime) {
        this.repeatEndsTime = repeatEndsTime;
    }


    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTimeslots() {
        timeslots = null;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean hasTimeslots(){
        return timeslots!=null;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public String getHostUserUid() {
        return hostUserUid;
    }

    public void setHostUserUid(String hostUserUid) {
        this.hostUserUid = hostUserUid;
    }


    public List<PhotoUrl> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<PhotoUrl> photos) {
        this.photos = photos;
    }

    public int getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean getIsAllDay() {
        return this.isAllDay;
    }

    @Override
    public boolean iTimeEquals(Event obj2) {
        return this.getEventUid().equals(obj2.getEventUid());
    }

    public int getShowLevel() {
        return showLevel;
    }

    public void setShowLevel(int showLevel) {
        this.showLevel = showLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<TimeSlot> getTimeslots() {
        return this.timeslots;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
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

    public static class TimeslotConverter implements PropertyConverter<List<TimeSlot> , String> {
        Gson gson = new Gson();
        @Override
        public List<TimeSlot> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<TimeSlot>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<TimeSlot> entityProperty) {
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

    public static class InviteeConverter implements PropertyConverter<List<Invitee> , String>{
        Gson gson = new Gson();
        @Override
        public List<Invitee> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<Invitee>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<Invitee> entityProperty) {
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
}
