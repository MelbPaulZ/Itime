package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.mvpview.event.EventCreateDurationMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 7/6/17.
 */

public class EventCreateDurationViewModel extends ItimeBaseViewModel {

    private String duration;
    private LocalPresenter<EventCreateDurationMvpView> presenter;

    public EventCreateDurationViewModel(LocalPresenter<EventCreateDurationMvpView> presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }


}
