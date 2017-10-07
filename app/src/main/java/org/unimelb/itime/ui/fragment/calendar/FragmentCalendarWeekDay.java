package org.unimelb.itime.ui.fragment.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;
import java.util.Date;

import david.itimecalendar.calendar.listeners.ITimeCalendarWeekDayViewListener;
import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.ui.CalendarConfig;
import david.itimecalendar.calendar.ui.monthview.DayViewBody;
import david.itimecalendar.calendar.ui.unitviews.DraggableEventView;
import david.itimecalendar.calendar.ui.weekview.WeekView;
import david.itimecalendar.calendar.util.MyCalendar;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarWeekDay extends ItimeBaseFragment<CalendarMvpView, EventCreatePresenter<CalendarMvpView>> implements CalendarMvpView, ToolbarInterface, FragmentCalendar.CalendarListener {
    private View root;
    private EventManager eventManager;
    private WeekView weekView;
    private FragmentCalendar.OnDateChanged onDateChanged;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar_weekday, container, false);
        eventManager = EventManager.getInstance(getContext());
        initView();
        return root;
    }

    @Override
    public EventCreatePresenter<CalendarMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
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
            weekView.setEventPackage(EventManager.getInstance(getContext()).getEventsPackage());
        }
    }

    private void initView(){
        weekView = (WeekView) root.findViewById(R.id.week_view);
        weekView.getCalendarConfig().enableEvent();
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        weekView.setEventPackage(eventManager.getEventsPackage());
        weekView.setITimeCalendarWeekDayViewListener(listener);
    }

    public FragmentCalendar.OnDateChanged getOnDateChanged() {
        return onDateChanged;
    }

    public void setOnDateChanged(FragmentCalendar.OnDateChanged onDateChanged) {
        this.onDateChanged = onDateChanged;
    }

    private ITimeCalendarWeekDayViewListener listener = new ITimeCalendarWeekDayViewListener(){

        @Override
        public void onDateChanged(Date date) {
            onDateChanged.onDateChanged(date);
        }

        @Override
        public boolean isDraggable(DraggableEventView draggableEventView) {
            Event event = (Event) draggableEventView.getEvent();
            return event.getEventType().equals(Event.EVENT_TYPE_SOLO);
        }

        @Override
        public void onEventCreate(DraggableEventView draggableEventView) {
            Intent intent = new Intent(getActivity(), EventCreateActivity.class);
            Event event = new Event();
            event.setStartTime(draggableEventView.getStartTimeM());
            event.setEndTime(draggableEventView.getEndTimeM());
            intent.putExtra("Event", event);
            startActivityForResult(intent, EventCreateActivity.CREATE_EVENT);
        }

        @Override
        public void onEventClick(DraggableEventView draggableEventView) {
            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
            intent.putExtra(EventDetailActivity.EVENT, (Event)draggableEventView.getEvent());
            getActivity().startActivity(intent);
        }

        @Override
        public void onEventDragStart(DraggableEventView draggableEventView) {

        }

        @Override
        public void onEventDragging(DraggableEventView draggableEventView, MyCalendar myCalendar, int i, int i1, int viewX, int viewY, String s) {

        }

        @Override
        public void onEventDragDrop(DraggableEventView draggableEventView) {
            final Event originEvent = (Event) draggableEventView.getEvent();
            EventUtil.updateSoloEvent(
                    originEvent,
                    draggableEventView.getStartTimeM(),
                    draggableEventView.getEndTimeM(),
                    presenter);
        }

        @Override
        public void onEventDragEnd(DraggableEventView draggableEventView) {

        }

        @Override
        public void onAllDayEventClick(ITimeEventInterface iTimeEventInterface) {
            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
            intent.putExtra(EventDetailActivity.EVENT, (Event)iTimeEventInterface);
            getActivity().startActivity(intent);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EventCreateActivity.CREATE_EVENT) {
            if (resultCode == Activity.RESULT_OK) {
                weekView.setEventPackage(EventManager.getInstance(getContext()).getEventsPackage());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        weekView.postDelayed(new Runnable() {
            @Override
            public void run() {
                weekView.scrollToDate(new Date(),true); // scroll time not working...
                weekView.refresh();
            }
        },500);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void backToToday(){
        if (weekView != null){
            weekView.scrollToDate(new Date(),false);
        }
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            default:
                weekView.refresh();
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void scrollToDate(Date date, boolean toTime) {
        if (weekView == null){
            return;
        }

        weekView.scrollToDate(date,toTime);
    }
}
