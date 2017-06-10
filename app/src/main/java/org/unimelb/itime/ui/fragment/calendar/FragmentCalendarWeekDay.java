package org.unimelb.itime.ui.fragment.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.mudules.monthview.DayViewBody;
import david.itimecalendar.calendar.mudules.weekview.WeekView;
import david.itimecalendar.calendar.unitviews.DraggableEventView;
import david.itimecalendar.calendar.util.MyCalendar;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarWeekDay extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface {
    private View root;
    private EventManager eventManager;
    private WeekView weekView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar_weekday, container, false);
        eventManager = EventManager.getInstance(getContext());
        initView();
        return root;
    }

    @Override
    public CalendarPresenter<CalendarMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

    }

    private void initView(){
        weekView = (WeekView) root.findViewById(R.id.week_view);
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        weekView.setEventPackage(eventManager.getEventsPackage());
        weekView.setOnBodyEventListener(new WeekViewBodyListener());
    }

    private class WeekViewBodyListener implements DayViewBody.OnViewBodyEventListener{

        @Override
        public boolean isDraggable(DraggableEventView draggableEventView) {
            return false;
        }

        @Override
        public void onEventCreate(DraggableEventView draggableEventView) {

        }

        @Override
        public void onEventClick(DraggableEventView draggableEventView) {

        }

        @Override
        public void onEventDragStart(DraggableEventView draggableEventView) {

        }

        @Override
        public void onEventDragging(DraggableEventView draggableEventView, MyCalendar myCalendar, int i, int i1) {

        }

        @Override
        public void onEventDragDrop(DraggableEventView draggableEventView) {

        }

        @Override
        public void onAllDayEventClick(ITimeEventInterface iTimeEventInterface) {

        }
    }
}
