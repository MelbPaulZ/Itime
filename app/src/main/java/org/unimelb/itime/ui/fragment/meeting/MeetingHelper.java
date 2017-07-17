package org.unimelb.itime.ui.fragment.meeting;

import org.unimelb.itime.bean.Meeting;

import static org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings.INVITATION_DETAILS;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public class MeetingHelper {

    public MeetingViewData getMeetingViewData(Meeting meeting){
        return new MeetingViewData();
    }

    public static class MeetingViewData{
        public int cardType = INVITATION_DETAILS;
        //left bar
        public String iconText;
        public int iconSrc;
        //
    }
}
