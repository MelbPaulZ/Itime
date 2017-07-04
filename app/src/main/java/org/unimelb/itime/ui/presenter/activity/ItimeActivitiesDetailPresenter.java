package org.unimelb.itime.ui.presenter.activity;

import android.content.Context;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesDetailMvpView;

/**
 * Created by Paul on 4/7/17.
 */

public class ItimeActivitiesDetailPresenter<V extends ItimeActivitiesDetailMvpView> extends ItimeBasePresenter<V> {
    public ItimeActivitiesDetailPresenter(Context context) {
        super(context);
    }
}
