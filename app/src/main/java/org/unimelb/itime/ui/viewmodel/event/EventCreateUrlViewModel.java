package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;

import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 6/6/17.
 */

public class EventCreateUrlViewModel extends BaseObservable{

    private LocalPresenter presenter;

    public EventCreateUrlViewModel(LocalPresenter presenter) {
        this.presenter = presenter;
    }
}
