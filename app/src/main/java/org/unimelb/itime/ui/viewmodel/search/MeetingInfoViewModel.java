package org.unimelb.itime.ui.viewmodel.search;

import android.databinding.Bindable;
import android.text.SpannableString;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.viewmodel.search.SpannableInfoViewModel;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class MeetingInfoViewModel extends SpannableInfoViewModel {
    private Meeting meeting;
    private Event event;
    private SpannableString titleStr;
    private SpannableString timeStr;

    public MeetingInfoViewModel(Meeting meeting) {
        setMeeting(meeting);
    }

    @Bindable
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
        setEvent(meeting.getEvent());

        setTitleStr(new SpannableString(event.getSummary()));
        setTimeStr(new SpannableString(EventUtil.getEventDateStr(event)));
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public SpannableString getTitleStr() {
        return changeMatchColor(event.getSummary(), getMatchStr());
    }

    public void setTitleStr(SpannableString titleStr) {
        this.titleStr = titleStr;
        notifyPropertyChanged(BR.titleStr);
    }

    @Bindable
    public SpannableString getTimeStr() {
        return new SpannableString(EventUtil.getEventDateStr(event));
    }

    public void setTimeStr(SpannableString timeStr) {
        this.timeStr = timeStr;
        notifyPropertyChanged(BR.timeStr);
    }
}
