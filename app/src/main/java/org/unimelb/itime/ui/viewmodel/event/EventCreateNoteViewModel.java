package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;
import android.databinding.Observable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 6/6/17.
 */

public class EventCreateNoteViewModel extends ItimeBaseViewModel {

    private LocalPresenter presenter;
    private Event event;
    private boolean requestFocus = true;
    public EventCreateNoteViewModel(LocalPresenter presenter) {
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
    public boolean getRequestFocus(){
        return this.requestFocus;
    }


}
