package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.database.Observable;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.sax.RootElement;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingBaseCardViewModel extends BaseObservable {
    private List<Meeting> meetings;
    private Meeting meeting;
    protected RecyclerViewAdapterMeetings.Mode mode;

    public MeetingBaseCardViewModel(RecyclerViewAdapterMeetings.Mode mode) {
        this.mode = mode;
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

    public String getSysMsg(){
        return meeting.getSysMsg();
    }

    public Drawable getIconSrc(Context context){
        // TODO: 27/06/2017 logic for icon
        return context.getResources().getDrawable(R.drawable.icon_meetings_vote);
    }

    public int getIconTextColor(){
        return R.color.white;
    }

    public String getIconText(){
        return "Vote";
    }

    public String getTitle(){
        return meeting.getEvent().getSummary();
    }

    public String getGreeting(){
        return meeting.getEvent().getGreeting();
    }

    public String getUpdatedTimeStr(){
        return "5:20";
    }

    public String getReminderTimeStr(){
        return "In 2d 2h";
    }

    public String getProfilePhotoUrl(){
        return "https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg";
    }

    public enum PictureMode{
        CIRCLE, DEFAULT
    }

//    @BindingAdapter({"bind:imageUrl"})
    @BindingAdapter( value={"imageUrl", "pictureMode"}, requireAll=false)
    public static void loadImage(ImageView view, String imageUrl, @Nullable PictureMode pictureMode) {
        RequestCreator creator = Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_image);

        if (pictureMode == null || pictureMode == PictureMode.DEFAULT){
            // no transform
        }else if (pictureMode == PictureMode.CIRCLE){
            creator.transform(new CropCircleTransformation());
        }

        creator.into(view);
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
        return context.getResources().getString(R.string.event_status_pending);
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

    public Drawable getEventStatusDrawable(Context context){
        GradientDrawable drawable = (GradientDrawable)context.getResources().getDrawable(R.drawable.itime_round_corner_card_status_bg);
        drawable.mutate();
        drawable.setColor(context.getResources().getColor(R.color.AliceBlue));
        return drawable;
    }

    public boolean getSwipeEnable(){
        return mode != RecyclerViewAdapterMeetings.Mode.COMING;
    }

    public boolean isRepeated(){
        if (meeting.getEvent().getRecurrence().length == 0){
            return false;
        }

        return true;
    }
}
