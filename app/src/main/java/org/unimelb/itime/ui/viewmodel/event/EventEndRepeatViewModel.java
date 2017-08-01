package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 5/6/17.
 */

public class EventEndRepeatViewModel extends BaseObservable {
    private LocalPresenter presenter;
    private int neverCheckVisibility;
    private int onDateCheckVisibility;
    private Event event;

    public EventEndRepeatViewModel(LocalPresenter presenter) {
        this.presenter = presenter;
        init();
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
    public int getNeverCheckVisibility() {
        return neverCheckVisibility;
    }

    public void setNeverCheckVisibility(int neverCheckVisibility) {
        this.neverCheckVisibility = neverCheckVisibility;
        notifyPropertyChanged(BR.neverCheckVisibility);
    }

    @Bindable
    public int getOnDateCheckVisibility() {
        return onDateCheckVisibility;
    }

    public void setOnDateCheckVisibility(int onDateCheckVisibility) {
        this.onDateCheckVisibility = onDateCheckVisibility;
        notifyPropertyChanged(BR.onDateCheckVisibility);
    }

    private void init(){
        neverCheckVisibility = View.GONE;
        onDateCheckVisibility = View.GONE;
    }

    public View.OnClickListener onClickNever(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neverCheckVisibility = neverCheckVisibility == View.VISIBLE ? View.GONE : View.VISIBLE;
                setNeverCheckVisibility(neverCheckVisibility);
                onDateCheckVisibility = View.GONE;
                setOnDateCheckVisibility(onDateCheckVisibility);
                event.getRule().setUntil(null, null);
                event.setRecurrence(event.getRule().getRecurrence());
                Log.i("", "onClick: ");
            }
        };
    }

    public View.OnClickListener onClickOndate(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateCheckVisibility = View.VISIBLE;
                setOnDateCheckVisibility(onDateCheckVisibility);
                neverCheckVisibility = View.GONE;
                setNeverCheckVisibility(neverCheckVisibility);

            }
        };
    }


}
