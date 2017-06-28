package org.unimelb.itime.ui.viewmodel.meeting;

import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;

/**
 * Created by yuhaoliu on 26/06/2017.
 */

public class MeetingHostingDetailCardViewModel extends MeetingBaseCardViewModel {

    public MeetingHostingDetailCardViewModel(RecyclerViewAdapterMeetings.Mode mode) {
        super(mode);
    }

    public String getLabelName_1(){
        return "Vote";
    }

    public String getLabelValue_1(){
        return "1";
    }

    public String getLabelName_2(){
        return "Vote";
    }

    public String getLabelValue_2(){
        return "2";
    }

    public String getLabelName_3(){
        return "Vote";
    }

    public String getLabelValue_3(){
        return "3";
    }
}
