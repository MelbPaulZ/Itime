package org.unimelb.itime.ui.fragment.calendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentCalendarTimeslotBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import david.itimecalendar.calendar.ui.monthview.DayViewBody;
import david.itimecalendar.calendar.ui.unitviews.DraggableTimeSlotView;
import david.itimecalendar.calendar.ui.unitviews.RecommendedSlotView;
import david.itimecalendar.calendar.ui.weekview.TimeSlotView;
import david.itimecalendar.calendar.util.MyCalendar;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarTimeslot extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface {
//    private View root;
    private FragmentCalendarTimeslotBinding binding;
    private EventManager eventManager;
    private TimeSlotView timeSlotView;
    private ToolbarViewModel toolbarVM;
    //For TESTING
    ArrayList<TimeSlot> slots = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * For TESTING
         */
        initSlots(slots);
        /*****/

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_timeslot, container, false);

        eventManager = EventManager.getInstance(getContext());
        initView();
        return binding.getRoot();
    }

    @Override
    public CalendarPresenter<CalendarMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbarVM = new ToolbarViewModel<>(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setTitle(getString(R.string.toolbar_timeslots));
        toolbarVM.setRightText(getString(R.string.new_event_toolbar_next));
        binding.setToolbarVM(toolbarVM);
    }

    @Override
    public void onNext() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    private void initView(){
        timeSlotView = (TimeSlotView) binding.getRoot().findViewById(R.id.timeslot_view);
        timeSlotView.enableTimeSlot();
        timeSlotView.setEventPackage(eventManager.getEventsPackage());
        timeSlotView.setOnTimeslotDurationChangedListener(new TimeSlotView.OnTimeslotDurationChangedListener() {
            @Override
            public void onTimeslotDurationChanged(long duration) {
                if (duration == -1){
                    //means all day
                    timeSlotView.setViewMode(DayViewBody.Mode.ALL_DAY);
                }else{
                    timeSlotView.setViewMode(DayViewBody.Mode.REGULAR);
                    timeSlotView.setTimeslotDuration(duration,false);
                }
            }
        });
        timeSlotView.setOnTimeSlotListener(new TimeslotViewBodyListener());
        timeSlotView.setTimeslotDurationItems(initList());

        for (TimeSlot slot:slots
                ) {
            timeSlotView.addTimeSlot(slot);
        }
    }

    private class TimeslotViewBodyListener implements DayViewBody.OnViewBodyTimeSlotListener{
        @Override
        public void onAllDayRcdTimeslotClick(long dayBeginMilliseconds) {
            TimeSlot newSlot = new TimeSlot();
            newSlot.setIsAllDay(true);
            //ensure set the start time correctly, otherwise it cannot be shown
            newSlot.setStartTime(dayBeginMilliseconds);
            timeSlotView.addTimeSlot(newSlot);
        }

        @Override
        public void onAllDayTimeslotClick(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotCreate(DraggableTimeSlotView draggableTimeSlotView) {
            TimeSlot newSlot = new TimeSlot();
            newSlot.setStartTime(draggableTimeSlotView.getNewStartTime());
            newSlot.setEndTime(draggableTimeSlotView.getNewEndTime());
            timeSlotView.addTimeSlot(newSlot);
        }

        @Override
        public void onTimeSlotClick(DraggableTimeSlotView draggableTimeSlotView) {
//                timeslotView.reloadTimeSlots(false);
        }


        @Override
        public void onRcdTimeSlotClick(RecommendedSlotView v) {
            v.getWrapper().setSelected(true);
            TimeSlot newSlot = new TimeSlot();
            newSlot.setStartTime(v.getWrapper().getTimeSlot().getStartTime());
            newSlot.setEndTime(v.getWrapper().getTimeSlot().getEndTime());
            timeSlotView.addTimeSlot(newSlot);
        }

        @Override
        public void onTimeSlotDragStart(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotDragging(DraggableTimeSlotView draggableTimeSlotView, MyCalendar curRealCal, int x, int y) {

        }

        @Override
        public void onTimeSlotDragDrop(DraggableTimeSlotView draggableTimeSlotView, long startTime, long endTime) {
            draggableTimeSlotView.getTimeslot().setStartTime(startTime);
            draggableTimeSlotView.getTimeslot().setEndTime(endTime);
        }

        @Override
        public void onTimeSlotEdit(DraggableTimeSlotView draggableTimeSlotView) {
            draggableTimeSlotView.getTimeslot().setStartTime(draggableTimeSlotView.getNewStartTime());
            draggableTimeSlotView.getTimeslot().setEndTime(draggableTimeSlotView.getNewEndTime());
        }

        @Override
        public void onTimeSlotDelete(DraggableTimeSlotView draggableTimeSlotView) {
            timeSlotView.removeTimeslot(draggableTimeSlotView.getWrapper());
        }

    }

    private List initList(){
        List<TimeSlotView.DurationItem> list= new ArrayList<>();
        int target = 5;
        for (int i = 1; i < target+1; i++) {
            TimeSlotView.DurationItem item = new TimeSlotView.DurationItem();
            if (i == target){
                item.showName = "All Day";
                item.duration = -1;
            }else{
                item.showName = "" + i + " hrs";
                item.duration = i * 3600 * 1000;
            }

            list.add(item);
        }

        return list;
    }

    private void initSlots(ArrayList<TimeSlot> slots){
        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long duration = 3*3600*1000;
        long dayInterval = 24 * 3600 * 1000;
        for (int i = 0; i < 10; i++) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(startTime);
            slot.setEndTime(startTime+duration);
            slot.setIsSystemSuggested(1);
            slot.setIsAllDay(i == 2);
            slots.add(slot);

            startTime += dayInterval;
        }
    }
}
