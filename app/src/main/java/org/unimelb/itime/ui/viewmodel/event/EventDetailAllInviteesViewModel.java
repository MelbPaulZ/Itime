package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/8/19.
 */

public class EventDetailAllInviteesViewModel extends BaseObservable {
    private int repliedNum = 0;
    private int cantGoNum = 0;
    private int noReplyNum = 0;
    private int inviteeNum = 0;
    private Event event;
    private boolean timeslotMode = false;
    private String labelString = "";
    private Context context;

    private List<Invitee> goingInvitees = new ArrayList<>();
    private List<Invitee> notGoingInvitees = new ArrayList<>();
    private List<Invitee> noReplyInvitees = new ArrayList<>();

    @Bindable
    public int getRepliedNum() {
        return repliedNum;
    }

    public void setRepliedNum(int repliedNum) {
        this.repliedNum = repliedNum;
        notifyPropertyChanged(BR.repliedNum);
    }

    @Bindable
    public boolean isTimeslotMode() {
        return timeslotMode;
    }

    public void setTimeslotMode(boolean timeslotMode) {
        this.timeslotMode = timeslotMode;
        notifyPropertyChanged(BR.timeslotMode);
    }

    @Bindable
    public int getCantGoNum() {
        return cantGoNum;
    }

    public void setCantGoNum(int cantGoNum) {
        this.cantGoNum = cantGoNum;
        notifyPropertyChanged(BR.cantGoNum);
    }

    @Bindable
    public int getNoReplyNum() {
        return noReplyNum;
    }

    public void setNoReplyNum(int noReplyNum) {
        this.noReplyNum = noReplyNum;
        notifyPropertyChanged(BR.noReplyNum);
    }

    @Bindable
    public int getInviteeNum() {
        return inviteeNum;
    }

    public void setInviteeNum(int inviteeNum) {
        this.inviteeNum = inviteeNum;
        notifyPropertyChanged(BR.inviteeNum);
    }

    public List<Invitee> getGoingInvitees() {
        return goingInvitees;
    }

    public List<Invitee> getNotGoingInvitees() {
        return notGoingInvitees;
    }

    public List<Invitee> getNoReplyInvitees() {
        return noReplyInvitees;
    }

    public void generateByEvent(Event event) {
        goingInvitees.clear();
        noReplyInvitees.clear();
        notGoingInvitees.clear();
        for (Invitee invitee : event.getInvitee().values()) {
            String status = invitee.getStatus();
            switch (status) {
                case Invitee.STATUS_ACCEPTED:
                    goingInvitees.add(invitee);
                    break;
                case Invitee.STATUS_NEEDSACTION:
                    noReplyInvitees.add(invitee);
                    break;
                case Invitee.STATUS_DECLINED:
                    notGoingInvitees.add(invitee);
                    break;
            }
        }
        setTimeslotMode(false);
        setRepliedNum(goingInvitees.size());
        setCantGoNum(notGoingInvitees.size());
        setNoReplyNum(noReplyInvitees.size());
        setInviteeNum(event.getInvitee().size());
    }

    public void generateByTimeSlot(Event event, TimeSlot timeSlot){
        goingInvitees.clear();
        noReplyInvitees.clear();
        notGoingInvitees.clear();

        goingInvitees.addAll(timeSlot.getVoteInvitees());
        notGoingInvitees.addAll(timeSlot.getRejectInvitees());
        for (Invitee invitee : event.getInvitee().values()) {
            String status = invitee.getStatus();
            switch (status) {
                case Invitee.STATUS_NEEDSACTION:
                    noReplyInvitees.add(invitee);
                    break;
            }
        }

        setTimeslotMode(true);
        setRepliedNum(goingInvitees.size());
        setCantGoNum(notGoingInvitees.size());
        setNoReplyNum(noReplyInvitees.size());
        setInviteeNum(event.getInvitee().size());
        setLabelString(String.format(context.getString(R.string.event_detail_voted_n_people_voted), getRepliedNum()));
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Bindable
    public String getLabelString() {
        return labelString;
    }

    public void setLabelString(String labelString) {
        this.labelString = labelString;
        notifyPropertyChanged(BR.labelString);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}