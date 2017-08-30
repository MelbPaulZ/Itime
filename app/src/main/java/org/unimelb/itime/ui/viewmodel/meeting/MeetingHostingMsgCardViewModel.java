package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.view.View;

import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.util.TimeFactory;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class MeetingHostingMsgCardViewModel extends MeetingHostingBaseCardViewModel {

    public MeetingHostingMsgCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode, MeetingPresenter<MeetingMvpView> meetingPresenter) {
        super(context,mode,meetingPresenter);
    }

    public String getSubTitle(){
        return "(" +
                TimeFactory.getFormatTimeString(meeting.getEvent().getStartTime(),TimeFactory.DAY_MONTH_YEAR)
                + ")";
    }

    public int getUpdatedTimeVisibility(){
        if (mode == RecyclerViewAdapterMeetings.Mode.COMING){
            return View.GONE;
        }

        return View.VISIBLE;
    }
}
