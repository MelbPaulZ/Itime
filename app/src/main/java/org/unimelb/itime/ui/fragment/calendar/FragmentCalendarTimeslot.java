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
import org.unimelb.itime.databinding.FragmentCalendarTimeslotBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import david.itimecalendar.calendar.mudules.monthview.DayViewBody;
import david.itimecalendar.calendar.mudules.weekview.TimeSlotView;
import david.itimecalendar.calendar.unitviews.DraggableTimeSlotView;
import david.itimecalendar.calendar.unitviews.RecommendedSlotView;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        root = inflater.inflate(R.layout.fragment_calendar_timeslot, container, false);
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
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        timeSlotView.setEventPackage(eventManager.getEventsPackage());
        timeSlotView.setOnTimeSlotListener(new TimeslotViewBodyListener());
    }

    private class TimeslotViewBodyListener implements DayViewBody.OnViewBodyTimeSlotListener{

        @Override
        public void onAllDayRcdTimeslotClick(long l) {

        }

        @Override
        public void onAllDayTimeslotClick(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotCreate(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotClick(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onRcdTimeSlotClick(RecommendedSlotView recommendedSlotView) {

        }

        @Override
        public void onTimeSlotDragStart(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotDragging(DraggableTimeSlotView draggableTimeSlotView, MyCalendar myCalendar, int i, int i1) {

        }

        @Override
        public void onTimeSlotDragDrop(DraggableTimeSlotView draggableTimeSlotView, long l, long l1) {

        }

        @Override
        public void onTimeSlotEdit(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotDelete(DraggableTimeSlotView draggableTimeSlotView) {

        }
    }
}
