package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.MeetingUtil;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class MeetingHostingDetailCardViewModel extends MeetingHostingBaseCardViewModel {
    //int[] {goingNum, notGoingNum, votedNum, cantGoNum, noReplyNum}
    private int[] inviteeVotedStatus;

    public MeetingHostingDetailCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode, MeetingPresenter<MeetingMvpView> meetingPresenter) {
        super(context, mode,meetingPresenter);
    }

    @Override
    public void setMeeting(Meeting meeting) {
        super.setMeeting(meeting);
        inviteeVotedStatus = MeetingUtil.getMeetingVotedStatus(meeting.getEvent());
    }

    public String getLabelName_1(){
        boolean confirmed = meeting.getEvent().getStatus().equals(Event.STATUS_CONFIRMED);
        return  confirmed ? "Going":"Voted";
    }

    public String getLabelValue_1(){
        boolean confirmed = meeting.getEvent().getStatus().equals(Event.STATUS_CONFIRMED);
        return  "" + (confirmed ?
                inviteeVotedStatus[0]
                :
                inviteeVotedStatus[2]);
    }

    public String getLabelName_2(){
        boolean confirmed = meeting.getEvent().getStatus().equals(Event.STATUS_CONFIRMED);
        return  confirmed ? "Not Going":"Can't go";
    }

    public String getLabelValue_2(){
        boolean confirmed = meeting.getEvent().getStatus().equals(Event.STATUS_CONFIRMED);
        return  "" + (confirmed ?
                inviteeVotedStatus[1]
                :
                inviteeVotedStatus[3]);
    }

    public String getLabelName_3(){
        boolean confirmed = meeting.getEvent().getStatus().equals(Event.STATUS_CONFIRMED);
        return  confirmed ? "No reply":"No reply";
    }

    public String getLabelValue_3(){
        boolean confirmed = meeting.getEvent().getStatus().equals(Event.STATUS_CONFIRMED);
        return  "" + (confirmed ?
                inviteeVotedStatus[4]
                :
                inviteeVotedStatus[4]);
    }
}
