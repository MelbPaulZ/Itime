package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by Paul on 21/6/17.
 */

public class TimeslotLineViewModel extends BaseObservable {
    private TimeSlot timeslot;
    private Context context;

    public TimeslotLineViewModel(Context context, TimeSlot timeslot) {
        this.context = context;
        this.timeslot = timeslot;
    }

    @Bindable
    public TimeSlot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(TimeSlot timeslot) {
        this.timeslot = timeslot;
        notifyPropertyChanged(BR.timeslot);
    }

    public String getStartTimeString(TimeSlot timeslot){
        String hourMin = EventUtil.getFormatTimeString(timeslot.getStartTime(), EventUtil.HOUR_MIN);
        return String.format(context.getString(R.string.event_timeslot_single_start_time), hourMin);
    }

    public String getEndTimeString(TimeSlot timeSlot){
        if (timeSlot.isAllDay()){
            return "24:00";
        }
        String hourMin = EventUtil.getFormatTimeString(timeSlot.getEndTime(), "kk:mm");
        return String.format(context.getString(R.string.event_timeslot_single_end_time), hourMin);
    }

    public String getDateString(TimeSlot timeslot){
        String dateString = EventUtil.getFormatTimeString(timeslot.getStartTime(), EventUtil.WEEK_DAY_MONTH);
        return String.format(context.getString(R.string.event_timeslot_single_day), dateString);
    }
}
