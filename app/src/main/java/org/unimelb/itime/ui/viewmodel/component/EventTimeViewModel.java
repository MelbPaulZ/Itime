package org.unimelb.itime.ui.viewmodel.component;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.ui.mvpview.component.Cancellable;

/**
 * Created by Paul on 19/6/17.
 */

public class EventTimeViewModel extends BaseObservable{
    private int selectTime;
    private Context context;
    public static int EVENT_TIME_START_TIME = 1;
    public static int EVENT_TIME_END_TIME = 2;
    private Cancellable cancellable;

    public EventTimeViewModel(Context context, Cancellable cancellable) {
        this.selectTime = EVENT_TIME_START_TIME;
        this.context = context;
        this.cancellable = cancellable;
    }

    @Bindable
    public int getSelectTime() {
        return selectTime;
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
                setSelectTime(EVENT_TIME_START_TIME);
            }
        };
    }

    public View.OnClickListener onClickEnd(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
