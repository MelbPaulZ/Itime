package org.unimelb.itime.ui.viewmodel.component;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.ItimeDialogShowChangeInterface;
import org.unimelb.itime.ui.mvpview.component.Cancellable;
import org.unimelb.itime.util.EventUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paul on 19/6/17.
 */

public class EventTimeViewModel extends BaseObservable{
    private int selectTime;
    private Context context;
    public static int EVENT_TIME_START_TIME = 1;
    public static int EVENT_TIME_END_TIME = 2;
    private Cancellable cancellable;
    private ItimeDialogShowChangeInterface itimeDialogShowChangeInterface;
    private Event event;
    private Date selectDate;

    public EventTimeViewModel(Context context, Object views) {
        this.selectTime = EVENT_TIME_START_TIME;
        this.context = context;
        this.cancellable = (Cancellable) views;
        this.itimeDialogShowChangeInterface = (ItimeDialogShowChangeInterface) views;
    }

    @Bindable
    public Date getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(Date selectDate) {
        this.selectDate = selectDate;
        notifyPropertyChanged(BR.selectDate);
    }

    public String getMonthYearString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EventUtil.MONTH_YEAR);
        String s = simpleDateFormat.format(date);
        return s;
    }

    public void onClickCalendarBack(){
        itimeDialogShowChangeInterface.onBackCalendar();
    }

    public void onClickCalendarNext(){
        itimeDialogShowChangeInterface.onNextCalendar();
    }

    @Bindable
    public int getSelectTime() {
        return selectTime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setSelectTime(int selectTime) {
        this.selectTime = selectTime;
        notifyPropertyChanged(BR.selectTime);
    }

    public int getStartTimeColor(int selectTime){
        return selectTime == EVENT_TIME_START_TIME ?
                context.getResources().getColor(R.color.azure) : context.getResources().getColor(R.color.pinkishGreyTwo);
    }

    public int getEndTimeColor(int selectTime){
        return selectTime == EVENT_TIME_END_TIME ?
                context.getResources().getColor(R.color.azure) : context.getResources().getColor(R.color.pinkishGreyTwo);
    }

    public View.OnClickListener onClickStart(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itimeDialogShowChangeInterface!=null){
                    itimeDialogShowChangeInterface.onShowPage(EVENT_TIME_START_TIME);
                }
                setSelectTime(EVENT_TIME_START_TIME);
            }
        };
    }

    public View.OnClickListener onClickEnd(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itimeDialogShowChangeInterface!=null){
                    itimeDialogShowChangeInterface.onShowPage(EVENT_TIME_END_TIME);
                }
                setSelectTime(EVENT_TIME_END_TIME);
            }
        };
    }

    public View.OnClickListener onClickCancel(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancellable!=null){
                    cancellable.cancel();
                }
            }
        };
    }

    public View.OnClickListener onClickSave(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancellable!=null){
                    cancellable.save();
                }
            }
        };
    }



}
