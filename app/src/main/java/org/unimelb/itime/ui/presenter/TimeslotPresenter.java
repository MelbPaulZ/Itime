package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.RecommandRequest;
import org.unimelb.itime.bean.RecommandTime;
import org.unimelb.itime.ui.mvpview.calendar.TimeslotMvpView;

/**
 * Created by yuhaoliu on 9/8/17.
 */

public class TimeslotPresenter <V extends TimeslotMvpView> extends ItimeBasePresenter<V> {

    public TimeslotPresenter(Context context) {
        super(context);
    }

    public void fetchRecommendedTimeslots(RecommandRequest recommandRequest){


    }
}
