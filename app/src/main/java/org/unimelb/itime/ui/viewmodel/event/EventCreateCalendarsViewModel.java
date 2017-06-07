package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.event.EventCreateCalendarsMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 7/6/17.
 */

public class EventCreateCalendarsViewModel extends ItimeBaseViewModel {
    private LocalPresenter<EventCreateCalendarsMvpView> presenter;
    private Event event;
    public ObservableList<RepeatLineViewModel> items = new ObservableArrayList<>();
    public ItemBinding<RepeatLineViewModel> itemBinding = ItemBinding.of(BR.repeatItem, R.layout.row_repeat);

    public EventCreateCalendarsViewModel(LocalPresenter<EventCreateCalendarsMvpView> presenter) {
        this.presenter = presenter;
        init();
    }

    private void init(){
        for (int i = 0; i < mockCalendars().length ; i++) {
            RepeatLineViewModel lineViewModel = new RepeatLineViewModel(mockCalendars()[i],
                    View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue), true);
            lineViewModel.setOnClickCallBack(new RepeatLineViewModel.OnClickCallBack() {
                @Override
                public void beforeOnClick(RepeatLineViewModel repeatLineViewModel) {
                    if (repeatLineViewModel.getIconVisibility() == View.GONE){
                        resetAllClick();
                        repeatLineViewModel.setIconVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onClickCustom() {
                    // no custom...
                }
            });
            items.add(lineViewModel);
        }

        items.get(0).setIconVisibility(View.VISIBLE);

    }

    private String[] mockCalendars(){
        String[] calendars = new String[4];
        calendars[0] = "iTime";
        calendars[1] = "Work";
        calendars[2] = "School";
        calendars[3] = "Personal";
        return calendars;
    }

    private void resetAllClick(){
        for (int i = 0; i < items.size(); i++){
            items.get(i).setIconVisibility(View.GONE);
        }
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }
}
