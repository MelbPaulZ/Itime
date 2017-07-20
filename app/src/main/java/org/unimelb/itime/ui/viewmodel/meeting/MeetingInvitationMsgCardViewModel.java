package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.view.View;

import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class MeetingInvitationMsgCardViewModel extends MeetingInvitationBaseCardViewModel {

    public MeetingInvitationMsgCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode) {
        super(context, mode);
    }

    public String getSubTitle(){
        return "This is a sub title for msg";
    }

    public int getUpdatedTimeVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.COMING){
            return View.GONE;
        }

        return View.VISIBLE;
    }
}
