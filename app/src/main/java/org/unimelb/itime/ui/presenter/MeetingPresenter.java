package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;

import java.util.List;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public class MeetingPresenter <V extends MeetingMvpView> extends ItimeBasePresenter<V> {
    private List<Meeting> invitationDataSet;
    private List<Meeting> hostingDataSet;
    private List<Meeting> comingDataSet;

    public static class FilterResult{
        public List<Meeting> invitationResult;
        public List<Meeting> hostingResult;
        public List<Meeting> comingResult;
    }

    public MeetingPresenter(Context context) {
        super(context);
    }



}
