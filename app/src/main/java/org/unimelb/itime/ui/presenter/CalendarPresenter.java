package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;

/**
 * Created by Paul on 5/6/17.
 */

public class CalendarPresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {
    public CalendarPresenter(Context context) {
        super(context);
    }
}
