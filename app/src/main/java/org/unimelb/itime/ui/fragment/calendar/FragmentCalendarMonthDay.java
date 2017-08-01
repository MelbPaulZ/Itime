package org.unimelb.itime.ui.fragment.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.Date;

import david.itimecalendar.calendar.listeners.ITimeCalendarMonthDayViewListener;
import david.itimecalendar.calendar.listeners.ITimeEventInterface;

import david.itimecalendar.calendar.ui.monthview.DayViewBody;
import david.itimecalendar.calendar.ui.monthview.MonthView;
import david.itimecalendar.calendar.ui.unitviews.DraggableEventView;
import david.itimecalendar.calendar.util.MyCalendar;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarMonthDay extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface {
    private static final String TAG = "lifecycle";
    private View root;
    private EventManager eventManager;
    private MonthView monthDayView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar_monthday, container, false);
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

    @Subscribe
    public void refreshEvent(MessageEvent msg){
        if (msg.task == MessageEvent.RELOAD_EVENT){
            monthDayView.setEventPackage(EventManager.getInstance(getContext()).getEventsPackage());
        }
    }



    private void initView(){
        monthDayView = (MonthView) root.findViewById(R.id.month_view);
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        monthDayView.setEventPackage(eventManager.getEventsPackage());
        monthDayView.setITimeCalendarMonthDayViewListener(listener);
    }

    private ITimeCalendarMonthDayViewListener listener = new ITimeCalendarMonthDayViewListener(){

        @Override
        public boolean isDraggable(DraggableEventView draggableEventView) {
            return true;
        }

        @Override
        public void onEventCreate(DraggableEventView draggableEventView) {
            Intent intent = new Intent(getActivity(), EventCreateActivity.class);
            Event event = new Event();
            event.setStartTime(draggableEventView.getStartTimeM());
            event.setEndTime(draggableEventView.getEndTimeM());
//            Event event = EventUtil.createEventFromInterface(draggableEventView.getEvent());
            intent.putExtra("Event", event);
            startActivity(intent);
        }

        @Override
        public void onEventClick(DraggableEventView draggableEventView) {
            // TODO: 27/7/17 To event detail
            Event event = (Event) draggableEventView.getEvent();
        }

        @Override
        public void onEventDragStart(DraggableEventView draggableEventView) {

        }

        @Override
        public void onEventDragging(DraggableEventView draggableEventView, MyCalendar myCalendar, int i, int i1, String locationTime) {

        }

        @Override
        public void onEventDragDrop(DraggableEventView draggableEventView) {
            Event event = (Event) draggableEventView.getEvent();
            event.setStartTime(draggableEventView.getStartTimeM());
            event.setEndTime(draggableEventView.getEndTimeM());
        }

        @Override
        public void onEventDragEnd(DraggableEventView draggableEventView) {

        }

        @Override
        public void onAllDayEventClick(ITimeEventInterface iTimeEventInterface) {
            // TODO: 27/7/17 To event detail
            Event event = (Event) iTimeEventInterface;
        }

        @Override
        public void onDateChanged(Date date) {
            //// TODO: 27/7/17 update header
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

        Log.i(TAG, "onStart: " + "FragmentCalendarMonthDay");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: " + "FragmentCalendarMonthDay");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: " + "FragmentCalendarMonthDay");
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
        Log.i(TAG, "onPause: " + "FragmentCalendarMonthDay");
    }
}
