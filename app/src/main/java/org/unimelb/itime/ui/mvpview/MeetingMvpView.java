package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.util.List;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public interface MeetingMvpView extends TaskBasedMvpView, ItimeBaseMvpView {
    void onDataLoaded(MeetingPresenter.FilterResult meetings);
    void onMeetingClick(Meeting meeting);
}
