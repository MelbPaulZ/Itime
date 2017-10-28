package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.component.EventTimeViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Paul on 15/6/17.
 */

public class EventCreatePrivateViewModel extends EventCreateViewModel {
    public EventCreatePrivateViewModel(EventCreatePresenter presenter, HashMap<String, Integer> orderHashMap) {
        super(presenter);
        setOrderHashMap(orderHashMap);
        init();
    }


    private void init(){
        ButtonItem buttonItem = createButtonItem(getString(R.string.alert_toolbar_btn));
        getButtonItems().add(buttonItem);
    }

    public View.OnClickListener onClickStartTime(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.showPopupDialog(EventTimeViewModel.EVENT_TIME_START_TIME);
                }
            }
        };
    }

    public View.OnClickListener onClickEndTime(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.showPopupDialog(EventTimeViewModel.EVENT_TIME_END_TIME);
                }
            }
        };
    }

    @Override
    public String getEventStartDate(Event event) {
        if (event.isAllDay()){
            Date d = EventUtil.parseTimeZoneToDate(event.getStart().getDate(), EventUtil.YEAR_MONTH_DAY);
            return EventUtil.getFormatTimeString(d.getTime(), EventUtil.getWeekDayMonthPattern());
        }
        return super.getEventStartDate(event);
    }

    @Override
    public String getEventEndDate(Event event) {
        if (event.isAllDay()){
            Date d = EventUtil.parseTimeZoneToDate(event.getEnd().getDate(), EventUtil.YEAR_MONTH_DAY);
            return EventUtil.getFormatTimeString(d.getTime(), EventUtil.getWeekDayMonthPattern());
        }
        return super.getEventEndDate(event);
    }


    @Override
    protected ButtonItem createButtonItem(String name) {
        if (name.equals(getString(R.string.alert_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_alert), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mvpView!=null){
                        mvpView.toAlert(event);
                    }
                }
            });
        }
        return super.createButtonItem(name);
    }

    @Override
    public void setEvent(Event event) {
        this.event = event;
        resetButtonsAndRows();
        notifyPropertyChanged(BR.event);
    }

    @Override
    protected void resetButtonsAndRows() {
        super.resetButtonsAndRows();
        if (event.getReminder()!=-1){
            addAlertToRow(EventUtil.reminderIntToString(presenter.getContext(), event.getReminder()));
            removeItem(buttonItems, getString(R.string.alert_toolbar_btn));
        }else{
            addButton(getString(R.string.alert_toolbar_btn));
            removeItem(rowItems, getString(R.string.alert_toolbar_btn));
        }
        notifyPropertyChanged(BR.rowItems);
        notifyPropertyChanged(BR.buttonItems);
    }

    @Override
    protected void addButton(String name) {
        super.addButton(name);
        if (name.equals(getString(R.string.alert_toolbar_btn))){
            if (isContainBtn(name)){
                return;
            }
            ButtonItem buttonItem = createButtonItem(getString(R.string.alert_toolbar_btn));
            getButtonItems().add(buttonItem);
        }

    }

    private void addAlertToRow(String alertString){
        if (containRow(getString(R.string.alert_toolbar_btn))){
            updateRow(getString(R.string.alert_toolbar_btn), alertString);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toAlert(event);
                }
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.alert_toolbar_btn));
                event.setReminder(-1);
                setEvent(event);
            }
        };

        addInList(getString(R.string.alert_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_alert),
                alertString, onClickListener, onDeleteListener);
        notifyPropertyChanged(BR.rowItems);
    }


    @Override
    protected ButtonItem createButtonItem(String name, Drawable drawable, View.OnClickListener onClickListener) {
        return super.createButtonItem(name, drawable, onClickListener);
    }
}
