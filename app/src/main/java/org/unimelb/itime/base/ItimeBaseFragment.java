package org.unimelb.itime.base;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 2/6/17.
 */

public abstract class ItimeBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {

    public ItimeBaseActivity getBaseActivity(){
        return (ItimeBaseActivity) getActivity();
    }
}