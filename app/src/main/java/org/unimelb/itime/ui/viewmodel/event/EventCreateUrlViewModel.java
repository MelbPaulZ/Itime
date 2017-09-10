package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 6/6/17.
 */

public class EventCreateUrlViewModel extends ItimeBaseViewModel{

    private LocalPresenter presenter;
    private Event event;
    private boolean isInitFocus = true;

    public EventCreateUrlViewModel(LocalPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public boolean isInitFocus() {
        return isInitFocus;
    }
}
