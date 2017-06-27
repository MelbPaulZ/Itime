package org.unimelb.itime.base;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 2/6/17.
 */

public abstract class ItimeBasePresenter<V extends MvpView> extends MvpBasePresenter<V> {
    private Context context;

    public ItimeBasePresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
