package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.event.EventGreetingMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 21/6/17.
 */

public class EventGreetingViewModel extends ItimeBaseViewModel {

    private Event event;
    private EventCreatePresenter presenter;
    private EventGreetingMvpView mvpView;

    private int EDIT_COUNT_LIMIT = 140;
    private int EDIT_COUNT_THRESHOLD = 20;
    private GreetingMessageInterface greetingMessageInterface;

    public EventGreetingViewModel(EventCreatePresenter<EventGreetingMvpView> presenter) {
        this.presenter = presenter;
        this.mvpView = presenter.getView();
    }

    public void setGreetingMessageInterface(GreetingMessageInterface greetingMessageInterface) {
        this.greetingMessageInterface = greetingMessageInterface;
    }

    public interface GreetingMessageInterface{
        void isTextLengthValid(boolean isValid);
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    public TextWatcher onGreetingChangeListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                event.setGreeting(s.toString());
                setEvent(event);
                if (greetingMessageInterface!=null){
                    greetingMessageInterface.isTextLengthValid(s.toString().length()<=EDIT_COUNT_LIMIT);
                }
            }
        };
    }

    public int getWordCountTextColor(Event event){
        if(event.getGreeting().length()>=EDIT_COUNT_LIMIT - EDIT_COUNT_THRESHOLD){
            return presenter.getContext().getResources().getColor(R.color.warmPink);
        }else{
            return presenter.getContext().getResources().getColor(R.color.pinkishGrey);
        }
    }

    public String getWordCountString(Event event){
        return (EDIT_COUNT_LIMIT - event.getGreeting().length()) + "";
    }
}
