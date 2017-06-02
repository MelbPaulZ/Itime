package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreatePresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {

    public EventCreatePresenter(Context context) {
        super(context);
    }

}
