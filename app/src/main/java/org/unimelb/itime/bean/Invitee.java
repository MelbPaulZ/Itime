package org.unimelb.itime.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import david.itimecalendar.calendar.listeners.ITimeInviteeInterface;
import org.greenrobot.greendao.annotation.Generated;
import org.unimelb.itime.util.AppUtil;

/**
 * Created by yuhaoliu on 10/09/2016.
 */
@Entity
public class Invitee implements ITimeUserInfoInterface, ITimeInviteeInterface, Serializable, Parcelable {
    private static final long serialVersionUID = -7635944932445335913L;

    public transient final static String STATUS_NEEDSACTION = "needsAction";
    public transient final static String STATUS_ACCEPTED = "accepted";
    public transient final static String STATUS_DECLINED = "declined";
    public transient final static String USER_STATUS_ACTIVATED = "activated";
    public transient final static String USER_STATUS_UNACTIVATED = "unactivated";
    public transient final static String DEFAULT_NO_USER_UID = "-1";

    /**
     * has responded
     */
    public transient final static int HAS_RESPONDED = 1;
    public transient final static int NON_HAS_RESPONDED = 0;

    private String eventUid = "";
    private String inviteeUid = "";
    private String userUid = "";
    private String userId = "";
    private String aliasName = "";
    private String aliasPhoto = "";
    private String status = "";
    private String reason = "";
    private String userStatus = "";
    private boolean isHost = false;



    @Convert(converter = Invitee.ContactConverter.class , columnType = String.class)
    private Contact contact = null;

    public static class ContactConverter implements PropertyConverter<Contact,String> {
        Gson gson = new Gson();

        @Override
        public Contact convertToEntityProperty(String databaseValue) {
            return gson.fromJson(databaseValue, Contact.class);
        }

        @Override
        public String convertToDatabaseValue(Contact entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    private boolean hasResponded = false;

    @Convert(converter = Invitee.UserConverter.class , columnType = String.class)
    private User user = new User();
    public static class UserConverter implements PropertyConverter<User,String> {
        Gson gson = new Gson();

        @Override
        public User convertToEntityProperty(String databaseValue) {
            return gson.fromJson(databaseValue, User.class);
        }

        @Override
        public String convertToDatabaseValue(User entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    @Override
    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Convert(converter = Invitee.SlotResponseConverter.class , columnType = String.class)
    private List<SlotResponse> inviteeTimeslot = new ArrayList<>();

    public static class SlotResponseConverter implements PropertyConverter<List<SlotResponse>,String> {
        Gson gson = new Gson();

        @Override
        public List<SlotResponse> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<SlotResponse>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<SlotResponse> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public List<SlotResponse> getSlotResponses() {
        return inviteeTimeslot;
    }

    public void setSlotResponses(ArrayList<SlotResponse> slotResponses) {
        this.inviteeTimeslot = slotResponses;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Nullable
    @Override
    public String getPhoto() {
        return getAliasPhoto();
    }

    @Override
    public String getName() {
        return getAliasName();
    }

    @Override
    public String getShowPhoto() {
        return getPhoto();
    }

    @Override
    public String getShowName() {
        return getName();
    }

    @Override
    public String getSecondInfo() {
        return userId;
    }

    @Override
    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
    }

    public String getAliasName() {
        if(contact!=null){
            return contact.getAliasName();
        }

        if(user!=null){
            return user.getPersonalAlias();
        }

        return "";
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAliasPhoto() {
        if(contact!=null){
            return contact.getAliasPhoto();
        }

        if(user!=null){
            return user.getPhoto();
        }

        return "";
    }

    public void setAliasPhoto(String aliasPhoto) {
        this.aliasPhoto = aliasPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventUid);
        dest.writeString(this.inviteeUid);
        dest.writeString(this.userUid);
        dest.writeString(this.userId);
        dest.writeString(this.aliasName);
        dest.writeString(this.aliasPhoto);
        dest.writeString(this.status);
        dest.writeString(this.reason);
        dest.writeString(this.userStatus);
        dest.writeList(this.inviteeTimeslot);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SlotResponse> getInviteeTimeslot() {
        return this.inviteeTimeslot;
    }

    public void setInviteeTimeslot(List<SlotResponse> inviteeTimeslot) {
        this.inviteeTimeslot = inviteeTimeslot;
    }

    public boolean getHasResponded() {
        return this.hasResponded;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public void setHasResponded(boolean hasResponded) {
        this.hasResponded = hasResponded;
    }

    public Invitee() {
        inviteeUid = AppUtil.generateUuid();
    }

    protected Invitee(Parcel in) {
        this.eventUid = in.readString();
        this.inviteeUid = in.readString();
        this.userUid = in.readString();
        this.userId = in.readString();
        this.aliasName = in.readString();
        this.aliasPhoto = in.readString();
        this.status = in.readString();
        this.reason = in.readString();
        this.userStatus = in.readString();
        this.inviteeTimeslot = new ArrayList<SlotResponse>();
        in.readList(this.inviteeTimeslot, SlotResponse.class.getClassLoader());
    }

    @Generated(hash = 1873565021)
    public Invitee(String eventUid, String inviteeUid, String userUid, String userId, String aliasName,
            String aliasPhoto, String status, String reason, String userStatus, boolean isHost, Contact contact,
            boolean hasResponded, User user, List<SlotResponse> inviteeTimeslot) {
        this.eventUid = eventUid;
        this.inviteeUid = inviteeUid;
        this.userUid = userUid;
        this.userId = userId;
        this.aliasName = aliasName;
        this.aliasPhoto = aliasPhoto;
        this.status = status;
        this.reason = reason;
        this.userStatus = userStatus;
        this.isHost = isHost;
        this.contact = contact;
        this.hasResponded = hasResponded;
        this.user = user;
        this.inviteeTimeslot = inviteeTimeslot;
    }



    public static final Creator<Invitee> CREATOR = new Creator<Invitee>() {
        @Override
        public Invitee createFromParcel(Parcel source) {
            return new Invitee(source);
        }

        @Override
        public Invitee[] newArray(int size) {
            return new Invitee[size];
        }
    };

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public boolean getIsHost() {
        return this.isHost;
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }
}
