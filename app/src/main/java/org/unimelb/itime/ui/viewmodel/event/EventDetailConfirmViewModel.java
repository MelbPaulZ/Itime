package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.mvpview.event.EventDetailConfirmMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.util.TimeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Qiushuo Huang on 2017/6/26.
 */

public class EventDetailConfirmViewModel extends BaseObservable {
    private Event event;
    private EventCreatePresenter presenter;
    private TimeSlot timeSlot;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String repeatTime;
    private String repeatUntil;
    private List<String> goInvitees = new ArrayList<>();
    private List<String> notGoInvitees = new ArrayList<>();
    private String altertTime;
    private EventDetailConfirmMvpView mvpView;
    private boolean showRepeat;

    public boolean isShowRepeat() {
        return showRepeat;
    }

    public void setShowRepeat(boolean showRepeat) {
        this.showRepeat = showRepeat;
    }

    public String getRepeatUntil() {
        return repeatUntil;
    }

    public void setRepeatUntil(String repeatUntil) {
        this.repeatUntil = repeatUntil;
    }

    public EventDetailConfirmMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(EventDetailConfirmMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Bindable
    public String getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(String repeatTime) {
        this.repeatTime = repeatTime;
    }

    @Bindable
    public List<String> getGoInvitees() {
        return goInvitees;
    }

    public void setGoInvitees(List<String> goInvitees) {
        this.goInvitees = goInvitees;
    }

    @Bindable
    public List<String> getNotGoInvitees() {
        return notGoInvitees;
    }

    public void setNotGoInvitees(List<String> notGoInvitees) {
        this.notGoInvitees = notGoInvitees;
    }

    @Bindable
    public String getAltertTime() {
        return altertTime;
    }

    public void setAltertTime(String altertTime) {
        this.altertTime = altertTime;
    }

    public EventDetailConfirmViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Bindable
    public EventCreatePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(EventCreatePresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        Locale locale = presenter.getContext().getResources().getConfiguration().locale;
        setStartDate(TimeFactory.getFormatTimeString(timeSlot.getStartTime(), TimeFactory.DAY_MONTH_YEAR, locale));
        setStartTime(TimeFactory.getFormatTimeString(timeSlot.getStartTime(), TimeFactory.HOUR_MIN_A, locale));
        setEndDate(TimeFactory.getFormatTimeString(timeSlot.getEndTime(), TimeFactory.DAY_MONTH_YEAR, locale));
        setEndTime(TimeFactory.getFormatTimeString(timeSlot.getEndTime(), TimeFactory.HOUR_MIN_A, locale));

        List<String> goInvitees = new ArrayList<>();
        List<String> notGoInvitees = new ArrayList<>();
        for(Invitee invitee:timeSlot.getVoteInvitees()){
           goInvitees.add(invitee.getShowPhoto());
        }

        for(Invitee invitee:timeSlot.getRejectInvitees()){
            notGoInvitees.add(invitee.getShowPhoto());
        }

        setGoInvitees(goInvitees);
        setNotGoInvitees(notGoInvitees);
    }

    @Bindable
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Bindable
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Bindable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Bindable
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public View.OnClickListener onConfirmClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null) {
                    mvpView.confirm();
                }
            }
        };
    }

    public View.OnClickListener onAlertClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.changeAlert();
            }
        };
    }
}
