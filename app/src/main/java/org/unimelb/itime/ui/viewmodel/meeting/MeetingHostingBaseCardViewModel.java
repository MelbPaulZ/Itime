package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;

import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingHostingBaseCardViewModel extends MeetingBaseCardViewModel {

    public MeetingHostingBaseCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode) {
        super(context,mode);
    }

    public String getTitle(){
        return meeting.getEvent().getSummary();
    }

    public String getGreeting(){
        return meeting.getEvent().getGreeting();
    }

}
