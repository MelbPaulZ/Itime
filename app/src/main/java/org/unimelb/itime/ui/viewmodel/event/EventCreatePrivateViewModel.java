package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;

import org.unimelb.itime.ui.presenter.EventCreatePresenter;

/**
 * Created by Paul on 15/6/17.
 */

public class EventCreatePrivateViewModel extends EventCreateViewModel {
    public EventCreatePrivateViewModel(EventCreatePresenter presenter) {
        super(presenter);
    }
}
