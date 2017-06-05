package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.base.ItimeBaseViewModel;

/**
 * Created by Paul on 5/6/17.
 */

public class LocalPresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {
    public LocalPresenter(Context context) {
        super(context);
    }
}
