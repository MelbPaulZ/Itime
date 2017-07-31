package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;
import java.util.List;

import static org.unimelb.itime.util.MeetingUtil.getMeetingUpdatedTimeStr;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingBaseCardViewModel extends BaseObservable {
    protected List<Meeting> meetings;
    protected Meeting meeting;
    protected Context context;
    protected RecyclerViewAdapterMeetings.Mode mode;

    public MeetingBaseCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode) {
        this.mode = mode;
        this.context = context;
    }

    @Bindable
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
        notifyPropertyChanged(BR.meeting);
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public boolean getSwipeEnable(){
        return mode != RecyclerViewAdapterMeetings.Mode.COMING;
    }

    public int getDateTextVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.COMING){
            int index = meetings.indexOf(meeting);
            if (index == 0){
                return View.VISIBLE;
            }

            long dayBegin1 = EventUtil.getDayBeginMilliseconds(meetings.get(index - 1).getEvent().getStartTime());
            long dayBegin2 = EventUtil.getDayBeginMilliseconds(meeting.getEvent().getStartTime());

            if (dayBegin1 != dayBegin2){
                return View.VISIBLE;
            }

            return View.GONE;
        }

        return View.GONE;
    }

    public int getDateTextColor(Context context){
        long time = meeting.getEvent().getStartTime();
        long currentDayTime = Calendar.getInstance().getTimeInMillis();
        if (EventUtil.getDatesRelationType(currentDayTime, time) == 0){
            return context.getResources().getColor(R.color.brand_main);
        }

        return context.getResources().getColor(R.color.black);
    }

    public String getDateText(){
        Event event = meeting.getEvent();
        return EventUtil.getEventDateStr(event);
    }


    public String getUpdatedTimeStr(){
        String time = getMeetingUpdatedTimeStr(meeting);
        return time == null ? "" : time;
    }

    public String getReminderTimeStr(){
        return "In 2d 2h";
    }

    public int getStatusBlockVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.COMING){
            return View.GONE;
        }

        return View.VISIBLE;
    }

    public int getEventStatusVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.HOSTING){
            return View.VISIBLE;
        }

        return View.GONE;
    }

    public String getEventStatusText(Context context){
        String status = meeting.getEvent().getStatus();
        if (status.equals(Event.STATUS_CONFIRMED)){
            return context.getResources().getString(R.string.event_status_confirmed);
        }else if (status.equals(Event.STATUS_PENDING)){
            return context.getResources().getString(R.string.event_status_pending);
        }else if (status.equals(Event.STATUS_CANCELLED)){
            return context.getResources().getString(R.string.event_status_cancel);
        }

        return "N/A";
    }

    public int getEventStatusColor(){
        String status = meeting.getEvent().getStatus();
        int color = 0;

        switch (status) {
            case Event.STATUS_CONFIRMED:
                color = R.color.brand_main;
                break;
            case Event.STATUS_PENDING:
                color = R.color.brand_highlight;
                break;
            case Event.STATUS_CANCELLED:
                color = R.color.shadow_onwhite;
                break;
        }

        return color;
    }

    public Drawable getEventStatusDrawable(Context context){
        GradientDrawable drawable = (GradientDrawable)context.getResources().getDrawable(R.drawable.itime_round_corner_card_status_bg);
        drawable.mutate();
        drawable.setColor(context.getResources().getColor(R.color.brand_main));
        return drawable;
    }

    public boolean isRepeated(){
        if (meeting.getEvent().getRecurrence().length == 0){
            return false;
        }

        return true;
    }

    @BindingAdapter( value={"bg_color"}, requireAll=false)
    public static void changeBgDrawableColor(View view, int color) {
        GradientDrawable db = (GradientDrawable) view.getBackground();
        db.mutate();
        db.setColor(view.getContext().getResources().getColor(color));
    }
}
