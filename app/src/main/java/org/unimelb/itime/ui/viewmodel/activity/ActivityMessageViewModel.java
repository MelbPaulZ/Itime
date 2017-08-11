package org.unimelb.itime.ui.viewmodel.activity;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.icu.util.TimeUnit;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paul on 4/7/17.
 */

public class ActivityMessageViewModel extends BaseObservable{
    private Message message;
    private ItimeActivitiesMvpView mvpView;
    private Context context;

    public ActivityMessageViewModel(Message message) {
        this.message = message;
    }

    public void setMvpView(ItimeActivitiesMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Bindable
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public void onClickViewMore(){
        if (mvpView!=null){
            mvpView.onClickViewMore(message.getMessageGroupUid());
        }
    }

    public String getTimeString(Message message){
        Date date = EventUtil.parseTimeZoneToDate(message.getUpdatedAt(), EventUtil.UPDATE_CREATE_AT);
        if (date == null){
            return "";
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar todayC = Calendar.getInstance();
        if (EventUtil.isSameDay(c, todayC)){
            return EventUtil.getFormatTimeString(c.getTimeInMillis(), EventUtil.HOUR_MIN_A);
        }else if (EventUtil.isYesterDay(c, todayC)){
            return context.getString(R.string.yesterday);
        }else{
            return EventUtil.getFormatTimeString(c.getTimeInMillis(), EventUtil.WEEK_DAY_MONTH);
        }
    }
}