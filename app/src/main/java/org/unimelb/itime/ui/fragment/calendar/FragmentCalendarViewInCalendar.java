package org.unimelb.itime.ui.fragment.calendar;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import org.unimelb.itime.databinding.FragmentCalendarMonthdayReviewBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import david.itimecalendar.calendar.listeners.ITimeCalendarMonthDayViewListener;
import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.ui.CalendarConfig;
import david.itimecalendar.calendar.ui.monthview.MonthView;
import david.itimecalendar.calendar.ui.unitviews.DraggableEventView;
import david.itimecalendar.calendar.util.MyCalendar;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarViewInCalendar extends ItimeBaseFragment<CalendarMvpView, EventCreatePresenter<CalendarMvpView>> implements CalendarMvpView, ToolbarInterface {
    private EventManager eventManager;
    private MonthView monthDayView;
    private FragmentCalendar.OnDateChanged onDateChanged;
    private ToolbarViewModel toolbarViewModel;
    private FragmentCalendarMonthdayReviewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_monthday_review, container, false);
        }
        eventManager = EventManager.getInstance(getContext());
        monthDayView = (MonthView) binding.getRoot().findViewById(R.id.month_view);

        initView();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitle(getString(R.string.toolbar_view_in_calendar));
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_back_black));
        binding.setToolbarVM(toolbarViewModel);
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
        getFragmentManager().popBackStack();
    }

    @Subscribe
    public void refreshEvent(MessageEvent msg){
        if (msg.task == MessageEvent.RELOAD_EVENT){
            monthDayView.setEventPackage(EventManager.getInstance(getContext()).getEventsPackage());
            monthDayView.refresh();
        }
    }

    public FragmentCalendar.OnDateChanged getOnDateChanged() {
        return onDateChanged;
    }

    public void setOnDateChanged(FragmentCalendar.OnDateChanged onDateChanged) {
        this.onDateChanged = onDateChanged;
    }

    private void initView(){
        CalendarConfig config = monthDayView.getCalendarConfig();
        config.enableEvent();
        config.isEventClickable = false;
        config.isEventCreatable = false;
        config.isEventDraggable = false;
        config.unconfirmedIncluded = true;
        config.isHeaderVisible = false;
        monthDayView.refresh();
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        monthDayView.setEventPackage(eventManager.getEventsPackage());
        monthDayView.setITimeCalendarMonthDayViewListener(listener);

        if (event != null){
            monthDayView.scrollToDate(new Date(event.getStartTime()),true);
        }
    }

    private ITimeCalendarMonthDayViewListener listener = new ITimeCalendarMonthDayViewListener(){

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
        public void onEventDragging(DraggableEventView draggableEventView, MyCalendar myCalendar, int i, int i1, String locationTime) {

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

        @Override
        public void onDateChanged(Date date) {
            if (onDateChanged != null){
                onDateChanged.onDateChanged(date);
            }
        }

        @Override
        public void onHeaderFlingDateChanged(Date date) {
            EventManager.getInstance(getContext()).syncRepeatedEvent(date.getTime());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EventCreateActivity.CREATE_EVENT) {
            if (resultCode == Activity.RESULT_OK) {
                monthDayView.setEventPackage(EventManager.getInstance(getContext()).getEventsPackage());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        monthDayView.refresh();
    }

    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            default:
                monthDayView.refresh();
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        if (monthDayView != null){
            monthDayView.scrollToDate(new Date(event.getStartTime()),true);
        }
    }
}
