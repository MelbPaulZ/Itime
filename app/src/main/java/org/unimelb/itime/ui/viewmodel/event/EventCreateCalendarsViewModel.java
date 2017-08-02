package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.event.EventCreateCalendarsMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.util.CalendarUtil;

import java.util.List;

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
        for (int i = 0; i < getCalendars().size() ; i++) {
            RepeatLineViewModel lineViewModel = new RepeatLineViewModel(getCalendars().get(i).getSummary(),
                    View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue), true);
            lineViewModel.setOnClickCallBack(new RepeatLineViewModel.OnClickCallBack() {
                @Override
                public void beforeOnClick(RepeatLineViewModel repeatLineViewModel) {
                    if (repeatLineViewModel.getIconVisibility() == View.GONE){
                        resetAllClick();
                        repeatLineViewModel.setIconVisibility(View.VISIBLE);
                        int index = items.indexOf(repeatLineViewModel);
                        event.setCalendarUid(getCalendars().get(index).getCalendarUid());
                    }
                }
                @Override
                public void onClickCustom() {
                    // no custom...
                }
            });
            items.add(lineViewModel);
        }
    }

    private void updateViews(){
        int firstShowIndex = 0;
        for (int i = 0 ; i < getCalendars().size() ; i ++){
            Calendar c = getCalendars().get(i);
            if (getCalendars().get(i).getCalendarUid().equals(event.getCalendarUid())){
                firstShowIndex = i;
                break;
            }
        }

        items.get(firstShowIndex).setIconVisibility(View.VISIBLE);
    }

    private List<Calendar> getCalendars(){
        return CalendarUtil.getInstance(presenter.getContext()).getCalendar();
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
        updateViews();
        notifyPropertyChanged(BR.event);
    }
}
