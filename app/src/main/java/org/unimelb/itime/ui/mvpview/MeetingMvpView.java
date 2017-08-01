package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.util.List;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public interface MeetingMvpView extends ItimeBaseMvpView {
    void onDataLoaded(MeetingPresenter.FilterResult meetings, List<Meeting> comingMeetings);
    void onMeetingClick(Meeting meeting);
}
