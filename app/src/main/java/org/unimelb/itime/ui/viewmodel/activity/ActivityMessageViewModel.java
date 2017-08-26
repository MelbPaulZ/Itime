package org.unimelb.itime.ui.viewmodel.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.icu.util.TimeUnit;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.activity.EventDetailActivity;
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

    public SpannableString getActivityDisplayMessage(Message message){
        String totalString = message.getDisplayName() + " " + message.getSubtitle1();
        SpannableString spannableString = new SpannableString(totalString);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, message.getDisplayName().length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#6d6d6d")), message.getDisplayName().length()+1, totalString.length(), 0);
        return spannableString;
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

    public View.OnClickListener onClickMessage(Message message){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                Event event = DBManager.getInstance(context).getEvent(message.getEventUid());
                if (event == null){
                    return;
                }
                intent.putExtra(EventDetailActivity.EVENT, event);
                context.startActivity(intent);
            }
        };
    }
}