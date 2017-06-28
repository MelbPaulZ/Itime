package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingMenu1ViewModel extends BaseObservable {

    private Meeting meeting;
    private OnMenuClick mOnMenuClick;

    public View.OnClickListener onClickPin(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onDataChange();
                }
            }
        };
    }
    public View.OnClickListener onClickMute(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onDataChange();
                }
            }
        };
    }

    public View.OnClickListener onClickArchive(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onDataChange();
                }
            }
        };
    }

    public interface OnMenuClick{
        void onDataChange();
    }

    public void setmOnMenuClick(OnMenuClick mOnMenuClick) {
        this.mOnMenuClick = mOnMenuClick;
    }

    @Bindable
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
        notifyPropertyChanged(BR.meeting);
    }

    public Drawable getPinIconSrc(Context context){
        return context.getResources().getDrawable(
                meeting.getEvent().isPin() ?
                        R.drawable.icon_meetings_swipeleft_unpin
                        :
                        R.drawable.icon_meetings_swipeleft_pin);
    }

    public Drawable getMuteIconSrc(Context context){
        return context.getResources().getDrawable(
                meeting.getEvent().isMute() ?
                        R.drawable.icon_meetings_swipeleft_unmute
                        :
                        R.drawable.icon_meetings_swipeleft_mute);
    }

    public Drawable getArchiveIconSrc(Context context){
        return context.getResources().getDrawable(
                meeting.getEvent().isArchive() ?
                        R.drawable.icon_meetings_swipeleft_archive
                        :
                        R.drawable.icon_meetings_swipeleft_archive);
    }

    public String getPinIconText(Context context){
        return context.getResources().getString(
                meeting.getEvent().isPin() ?
                        R.string.unpin
                        :
                        R.string.pin);
    }

    public String getMuteIconText(Context context){
        return context.getResources().getString(
                meeting.getEvent().isMute() ?
                        R.string.unmute
                        :
                        R.string.mute);
    }

    public String getArchiveIconText(Context context){
        return context.getResources().getString(
                meeting.getEvent().isArchive() ?
                        R.string.archive
                        :
                        R.string.archive);
    }
}
