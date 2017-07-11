package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchPresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {

    public SearchPresenter(Context context) {
        super(context);
    }
}
