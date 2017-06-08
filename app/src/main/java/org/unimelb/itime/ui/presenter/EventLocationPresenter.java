package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;

/**
 * Created by Paul on 8/6/17.
 */

public class EventLocationPresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {
    public EventLocationPresenter(Context context) {
        super(context);
    }
}
