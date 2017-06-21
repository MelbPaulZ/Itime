package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.TimeSlot;

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

    public String getTimeString(TimeSlot timeslot){
        String startHour = "12";
        String startMin = "00";
        String endHour = "13";
        String endMin = "30";
        return String.format(context.getString(R.string.event_timeslot_single_time), startHour, startMin, endHour, endMin);
    }

    public String getDateString(TimeSlot timeslot){
        String dayOfWeek = "WED";
        String dateOfMonth = "20";
        String month = "JUL";
        return String.format(context.getString(R.string.event_timeslot_single_day), dayOfWeek, dateOfMonth, month);
    }
}
