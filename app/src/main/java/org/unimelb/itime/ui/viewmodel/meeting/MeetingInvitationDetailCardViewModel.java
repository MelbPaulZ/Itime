package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.view.View;

import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class MeetingInvitationDetailCardViewModel extends MeetingInvitationBaseCardViewModel {

    public MeetingInvitationDetailCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode, MeetingPresenter<MeetingMvpView> meetingPresenter) {
        super(context,mode,meetingPresenter);
    }

    public int getUpdatedTimeVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.COMING){
            return View.GONE;
        }

        return View.VISIBLE;
    }

    public int getSideBarColor(){
        return cardTemplate.sidebarColor;
    }
}
