package org.unimelb.itime.bean;


import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Transient;
import org.unimelb.itime.util.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import david.itimecalendar.calendar.listeners.ITimeTimeSlotInterface;

/**
 * Created by Paul on 10/09/2016.
 */
public class TimeSlot implements ITimeTimeSlotInterface<TimeSlot>,Serializable {
    public static final long serialVersionUID = 536871006;
    public final static String STATUS_CREATING = "creating";
    public final static String STATUS_PENDING = "pending";
    public final static String STATUS_ACCEPTED = "accepted";
    public final static String STATUS_REJECTED = "rejected";

    private String timeslotUid = AppUtil.generateUuid(); //
    private String eventUid = ""; //
    private String userUid = "";
    private long startTime; //
    private long endTime; //
    private String status = ""; //
    private int acceptedNum; //
    private int totalNum; //
    private int rejectedNum; //
    private int pendingNum; //
    private boolean isConfirmed; //
    private boolean isSystemSuggested; // 1 -> true
    private String inviteeUid = "";
    private boolean isAllDay = false;
    @Transient
    private List<Invitee> voteInvitees = new ArrayList<>();

    @Override
    public void setStartTime(long l) {
        this.startTime = l;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public void setEndTime(long l) {
        this.endTime = l;
    }

    @Override
    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public void setStatus(String s) {
        this.status = s;
    }

    @Override
    public String getStatus() {
        return this.status;
    }
    public int getAcceptedNum() {
        return acceptedNum;
    }

    @Override
    public void setAcceptedNum(int i) {
        this.acceptedNum = i;
    }

    @Override
    public int getTotalNum() {
        return totalNum;
    }

    @Override
    public void setTotalNum(int i) {
        this.totalNum = i;
    }

    @Override
    public String getTimeslotUid() {
        return timeslotUid;
    }

    @Override
    public boolean isRecommended() {
        return isSystemSuggested;
    }

    @Override
    public void setIsAllDay(boolean b) {
        isAllDay = b;
    }

    @Override
    public boolean isAllDay() {
        return isAllDay;
    }

    public void setTimeslotUid(String timeslotUid) {
        this.timeslotUid = timeslotUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public int getRejectedNum() {
        return rejectedNum;
    }

    public void setRejectedNum(int rejectedNum) {
        this.rejectedNum = rejectedNum;
    }


    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public int getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(int pendingNum) {
        this.pendingNum = pendingNum;
    }

    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
    }

    public List<Invitee> getVoteInvitees() {
        return voteInvitees;
    }

    public void setVoteInvitees(List<Invitee> voteInvitees) {
        this.voteInvitees = voteInvitees;
    }

    @Override
    public int compareTo(@NonNull TimeSlot o) {
        long selfStartTime = this.getStartTime();
        long cmpTgtStartTime = o.getStartTime();
        int result = selfStartTime < cmpTgtStartTime ? -1 : 1;

        if (result == -1){
            return result;
        }else {
            return selfStartTime == cmpTgtStartTime ? 0 : 1;
        }
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isSystemSuggested() {
        return isSystemSuggested;
    }

    public void setSystemSuggested(boolean systemSuggested) {
        isSystemSuggested = systemSuggested;
    }
}
