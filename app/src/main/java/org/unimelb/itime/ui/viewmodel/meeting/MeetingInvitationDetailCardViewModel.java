package org.unimelb.itime.ui.viewmodel.meeting;

import android.view.View;

import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class MeetingInvitationDetailCardViewModel extends MeetingBaseCardViewModel {

    public MeetingInvitationDetailCardViewModel(RecyclerViewAdapterMeetings.Mode mode) {
        super(mode);
    }

    public int getUpdatedTimeVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.COMING){
            return View.GONE;
        }

        return View.VISIBLE;
    }
}
