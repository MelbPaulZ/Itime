package org.unimelb.itime.ui.viewmodel.search;

import android.databinding.Bindable;
import android.text.SpannableString;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class EventInfoViewModel extends SpannableInfoViewModel {
    private Event event;
    private SpannableString titleStr;
    private SpannableString timeStr;

    public EventInfoViewModel(Event event) {
        setEvent(event);
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        setTitleStr(new SpannableString(event.getSummary()));
        setTimeStr(new SpannableString(EventUtil.getEventDateStr(event)));
    }

    @Bindable
    public SpannableString getTitleStr() {
        return changeMatchColor(event.getSummary(), getMatchStr());
    }

    public void setTitleStr(SpannableString titleStr) {
        this.titleStr = titleStr;
        notifyPropertyChanged(BR.titleStr);
    }

    @Bindable
    public SpannableString getTimeStr() {
        return new SpannableString(EventUtil.getEventDateStr(event));
    }

    public void setTimeStr(SpannableString timeStr) {
        this.timeStr = timeStr;
        notifyPropertyChanged(BR.timeStr);
    }
}
