package org.unimelb.itime.ui.presenter.activity;

import android.content.Context;
import android.util.Log;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesDetailMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 4/7/17.
 */

public class ItimeActivitiesDetailPresenter<V extends ItimeActivitiesDetailMvpView> extends EventCreatePresenter<V> {
    private EventApi eventApi;
    public ItimeActivitiesDetailPresenter(Context context) {
        super(context);
        eventApi = HttpUtil.createService(getContext(), EventApi.class);
    }

    public String TAG = "ItimeActivitiesDetail";

    public void muteEvent(Event event, boolean isMute) {
        super.muteEvent(event, isMute);
    }
}
