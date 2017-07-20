package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.ImageFormat;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingMenuViewModel extends BaseObservable {
    public static final int NORMAL = 1;
    public static final int ARCHIVE = 2;

    private Meeting meeting;

    private RecyclerViewAdapterMeetings.OnMenuListener mOnMenuClick;

    public int getMenuVisibility(int type){
        int targetType;

        if (meeting.getEvent().isArchived()){
            targetType = ARCHIVE;
        }else {
            targetType = NORMAL;
        }
        return type == targetType ? View.VISIBLE : View.GONE;
    }


    public View.OnClickListener onClickPin(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onPin(meeting);
                }
            }
        };
    }
    public View.OnClickListener onClickMute(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onMute(meeting);
                }
            }
        };
    }

    public View.OnClickListener onClickArchive(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onArchive(meeting);
                }
            }
        };
    }

    public View.OnClickListener onClickDelete(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onDelete(meeting);
                }
            }
        };
    }

    public View.OnClickListener onClickRestore(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOnMenuClick!=null){
                    mOnMenuClick.onRestore(meeting);
                }
            }
        };
    }

    public void setmOnMenuClick(RecyclerViewAdapterMeetings.OnMenuListener mOnMenuClick) {
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
                meeting.getEvent().isPinned() ?
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
                meeting.getEvent().isArchived() ?
                        R.drawable.icon_meetings_swipeleft_archive
                        :
                        R.drawable.icon_meetings_swipeleft_archive);
    }

    public Drawable getDeleteIconSrc(Context context){
        return context.getResources().getDrawable(R.drawable.icon_meetings_delete);
    }

    public Drawable getRestoreIconSrc(Context context){
        return context.getResources().getDrawable(R.drawable.icon_meetings_restore);
    }

    public String getPinIconText(Context context){
        return context.getResources().getString(
                meeting.getEvent().isPinned() ?
                        R.string.unpin
                        :
                        R.string.pinned);
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
                meeting.getEvent().isArchived() ?
                        R.string.archived
                        :
                        R.string.archived);
    }

    public String getDeleteIconText(Context context){
        return context.getResources().getString(R.string.delete);
    }

    public String getRestoreIconText(Context context){
        return context.getResources().getString(R.string.restore);
    }
}
