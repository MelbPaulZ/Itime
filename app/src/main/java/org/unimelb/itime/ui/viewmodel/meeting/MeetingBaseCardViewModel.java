package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.database.Observable;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.sax.RootElement;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingBaseCardViewModel extends BaseObservable {
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

    public String getSysMsg(){
        return meeting.getSysMsg();
    }

    public Drawable getIconSrc(Context context){
        // TODO: 27/06/2017 logics for icon
        return context.getResources().getDrawable(R.drawable.icon_mute);
    }

    public int getIconTextColor(){
        return R.color.gray_color;
    }

    public String getIconText(){
        return "Cancelled";
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

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .into(view);
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
