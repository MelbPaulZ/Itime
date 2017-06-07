package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 5/6/17.
 */

public class EventRepeatCustomViewModel extends BaseObservable {
    private LocalPresenter presenter;

    private String frequencyString;
    private String gapString;
    private Event event;

    public EventRepeatCustomViewModel(LocalPresenter presenter) {
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
    public String getFrequencyString() {
        return frequencyString;
    }

    public void setFrequencyString(String frequencyString) {
        this.frequencyString = frequencyString;
        notifyPropertyChanged(BR.frequencyString);
    }

    @Bindable
    public String getGapString() {
        return gapString;
    }

    public void setGapString(String gapString) {
        this.gapString = gapString;
        notifyPropertyChanged(BR.gapString);
    }
}
