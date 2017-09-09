package org.unimelb.itime.ui.fragment.calendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentCalendarTimeslotBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventDetail;
import org.unimelb.itime.ui.fragment.event.FragmentEventDetailConfirm;
import org.unimelb.itime.ui.mvpview.calendar.TimeslotMvpView;
import org.unimelb.itime.ui.presenter.TimeslotPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarTimeslotViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.listeners.ITimeCalendarTimeslotViewListener;
import david.itimecalendar.calendar.ui.unitviews.DraggableTimeSlotView;
import david.itimecalendar.calendar.ui.unitviews.RcdRegularTimeSlotView;
import david.itimecalendar.calendar.ui.weekview.TimeSlotView;
import david.itimecalendar.calendar.util.MyCalendar;
import david.itimecalendar.calendar.wrapper.WrapperTimeSlot;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarTimeslot extends ItimeBaseFragment<TimeslotMvpView, TimeslotPresenter<TimeslotMvpView>>
        implements TimeslotMvpView, ToolbarTimeslotViewModel.ToolbarTimeSlotComponents {

    public enum Mode{
        HOST_CREATE, HOST_CONFIRM, INVITEE_CONFIRM
    }

    private FragmentCalendarTimeslotBinding binding;
    private EventManager eventManager;
    private TimeSlotView timeSlotView;
    private ToolbarTimeslotViewModel toolbarVM;
    private Event event;
    private Mode mode = Mode.HOST_CREATE;

    private int selectMax = 1;

    private transient List<TimeSlot> preSelectedSlots = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_timeslot, container, false);
        eventManager = EventManager.getInstance(getContext());
        initView();
        return binding.getRoot();
    }

    @NonNull
    @Override
    public TimeslotPresenter<TimeslotMvpView> createPresenter() {
        return new TimeslotPresenter<>(getContext());
    }

    public void setEvent(Event event, List<TimeSlot> selectedTimeslots) {
        this.event = event;
        if (selectedTimeslots != null){
            this.preSelectedSlots = selectedTimeslots;
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarVM = new ToolbarTimeslotViewModel<>(this);
        binding.setToolbarVM(toolbarVM);
        setTimeslotViewMode(mode,event);
        linkEventTimeslots(event, preSelectedSlots);



        //scroll to latest timeslot
        if (event.getTimeslot() == null || event.getTimeslot().size() == 0){
            return;
        }
        TimeSlot[] timeSlots = EventUtil.getNearestTimeslot(event.getTimeslot());
        TimeSlot targetTimeSlot = timeSlots[1] != null ? timeSlots[1]:timeSlots[0];
        timeSlotView.scrollToDate(new Date(targetTimeSlot.getStartTime()),true);
    }

    @Override
    public TimeSlotView getTimeSlotView() {
        return this.timeSlotView;
    }

    @Override
    public Mode getTimeSlotViewMode() {
        return this.mode;
    }

    @Override
    public void onNext() {
        Fragment fragment = getFrom();

        switch (mode){
            case HOST_CREATE:{
                event.setTimeslot(selectedTimeslotsMap);
                event.setDuration(selectDuration);
                ((FragmentEventCreate) fragment).setEvent(event);
                getFragmentManager().popBackStack();
                break;
            }
            case HOST_CONFIRM:{
                FragmentEventDetailConfirm fragmentConfirm = new FragmentEventDetailConfirm();
                fragmentConfirm.setEvent(event);
                fragmentConfirm.setTimeSlot(toolbarVM.getConfirmedTimeslot());
                getBaseActivity().openFragment(fragmentConfirm);
                break;
            }

            case INVITEE_CONFIRM:{
                ((FragmentEventDetail) fragment).setSelectedTimeslot(new ArrayList<>(selectedTimeslotsMap.values()));
                getFragmentManager().popBackStack();
                break;
            }
        }
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTimeslotSwitcherClick() {

    }

    @Subscribe
    public void refreshEvent(MessageEvent msg){
        if (msg.task == MessageEvent.RELOAD_EVENT){
            timeSlotView.setEventPackage(EventManager.getInstance(getContext()).getEventsPackage());
        }
    }

    private int selectDuration = 60; // default one hour

    private void initView(){
        timeSlotView = (TimeSlotView) binding.getRoot().findViewById(R.id.timeslot_view);
        // ensure set config before set mode
        timeSlotView.getCalendarConfig().unconfirmedIncluded = false;

        timeSlotView.setEventPackage(eventManager.getEventsPackage());
        timeSlotView.setOnTimeslotDurationChangedListener(duration -> {
            if (duration == -1){
                //switch to all day mode
                selectDuration = EventUtil.allDayMinutes;
                event.setIsAllDay(true);
                timeSlotView.setViewMode(TimeSlotView.ViewMode.ALL_DAY_CREATE);
            }else{
                selectDuration = (int)duration/1000/60;
                event.setIsAllDay(false);
                timeSlotView.setViewMode(TimeSlotView.ViewMode.NON_ALL_DAY_CREATE);
                timeSlotView.setTimeslotDuration(duration,false);
            }
        });
        timeSlotView.setITimeCalendarTimeslotViewListener(listener);
        timeSlotView.setTimeslotDurationItems(initList());
    }

    private void setTimeslotViewMode(Mode upperMode, Event event){
        List<TimeSlot> slots = new ArrayList<>(event.getTimeslot().values());
        TimeSlotView.ViewMode mode;
        switch (upperMode){
            case HOST_CREATE:
                if (slots.size() > 0 && slots.get(0).isAllDay()){
                    mode = TimeSlotView.ViewMode.ALL_DAY_CREATE;
                }else {
                    mode = TimeSlotView.ViewMode.NON_ALL_DAY_CREATE;
                }

                selectMax = getContext().getResources().getInteger(R.integer.timeslot_create_max_count);
                break;
            case HOST_CONFIRM:
                if (slots.size() > 0 && slots.get(0).isAllDay()){
                    mode = TimeSlotView.ViewMode.ALL_DAY_SELECT;
                }else {
                    mode = TimeSlotView.ViewMode.NON_ALL_DAY_SELECT;
                }
                selectMax = getContext().getResources().getInteger(R.integer.timeslot_confirm_max_count);
                break;
            case INVITEE_CONFIRM:
                if (slots.size() > 0 && slots.get(0).isAllDay()){
                    mode = TimeSlotView.ViewMode.ALL_DAY_SELECT;
                }else {
                    mode = TimeSlotView.ViewMode.NON_ALL_DAY_SELECT;
                }
                selectMax = event.getTimeslot().size();
                toolbarVM.setMaxCount(selectMax);
                break;
            default:
                mode = null;
        }

        timeSlotView.setViewMode(mode);
    }

    private void linkEventTimeslots(Event event, List<TimeSlot> selectedTimeslots){
        List<TimeSlot> timeSlots = new ArrayList<>(event.getTimeslot().values());

        //upgrade map
        switch (mode){
            case HOST_CREATE:
                break;
            case HOST_CONFIRM:
                if (selectedTimeslots.size() > 0){
                    toolbarVM.setConfirmedTimeslot(selectedTimeslots.get(0));
                }
                break;
            case INVITEE_CONFIRM:
                break;
        }


        for (TimeSlot timeslot:selectedTimeslots
                ) {
            selectedTimeslotsMap.put(timeslot.getTimeslotUid(), timeslot);
        }

        // add to timeslot view for display
        for (TimeSlot timeslot:timeSlots
                ) {
            WrapperTimeSlot wrapper = new WrapperTimeSlot(timeslot);
            wrapper.setSelected(selectedTimeslotsMap.containsKey(wrapper.getTimeSlot().getTimeslotUid()));
            //For timeslot view
            timeSlotView.addTimeSlot(wrapper);
        }

        updateToolbarTimeslot();
    }

    private Map<String, TimeSlot> selectedTimeslotsMap = new HashMap<>();

    private void updateToolbarTimeslot(){
        toolbarVM.setSelectedTimeslot(new ArrayList<>(selectedTimeslotsMap.values()));
    }

    private ITimeCalendarTimeslotViewListener listener = new ITimeCalendarTimeslotViewListener(){
        @Override
        public void onAllDayRcdTimeslotClick(long dayBeginMilliseconds) {
            TimeSlot newSlot = new TimeSlot();
            newSlot.setIsAllDay(true);
            //ensure set the start time correctly, otherwise it cannot be shown
            newSlot.setStartTime(dayBeginMilliseconds);
            newSlot.setEndTime(dayBeginMilliseconds);
            timeSlotView.addTimeSlot(newSlot);

            selectedTimeslotsMap.put(newSlot.getTimeslotUid(),newSlot);
            updateToolbarTimeslot();
        }

        @Override
        public void onAllDayTimeslotClick(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotCreate(DraggableTimeSlotView draggableTimeSlotView) {
            if (EventUtil.isExpired(draggableTimeSlotView.getNewStartTime())){
                Toast.makeText(getContext(), R.string.timeslot_alert_outdated_create, Toast.LENGTH_LONG).show();
            }else {
                if (selectedTimeslotsMap.size() < selectMax){
                    TimeSlot newSlot = new TimeSlot();
                    newSlot.setStartTime(draggableTimeSlotView.getNewStartTime());
                    newSlot.setEndTime(draggableTimeSlotView.getNewEndTime());
                    timeSlotView.addTimeSlot(newSlot);

                    selectedTimeslotsMap.put(newSlot.getTimeslotUid(),newSlot);
                    updateToolbarTimeslot();
                }else {
                    Toast.makeText(getContext(), String.format(getContext().getString(R.string.timeslot_alert_create_max_count), selectMax)
                            , Toast.LENGTH_LONG).show();
                }
            }
        }

        /**
         * Only regular timeslot click calls this function
         * @param draggableTimeSlotView
         */
        @Override
        public void onTimeSlotClick(DraggableTimeSlotView draggableTimeSlotView) {
            TimeSlot timeSlot = (TimeSlot) draggableTimeSlotView.getTimeslot();


            if (EventUtil.isExpired(draggableTimeSlotView.getTimeslot())){
                Toast.makeText(getContext(), R.string.timeslot_alert_outdated_select, Toast.LENGTH_LONG).show();
                return;
            }

            switch (mode){
                case HOST_CONFIRM:{
                    WrapperTimeSlot wrapperTimeSlot = draggableTimeSlotView.getWrapper();
                    boolean isSelected = !wrapperTimeSlot.isSelected();

                    if (isSelected){
                        if (draggableTimeSlotView.getWrapper().isConflict()){
                            Toast.makeText(getContext(), R.string.timeslot_alert_conflict_select, Toast.LENGTH_SHORT).show();
                        }

                        if (toolbarVM.getConfirmedTimeslot() == null){
                            toolbarVM.setConfirmedTimeslot(timeSlot);
                            wrapperTimeSlot.setSelected(true);
                        }else {
                            Toast.makeText(getContext()
                                    , String.format(getContext().getString(R.string.timeslot_alert_selected_max_count), selectMax)
                                    , Toast.LENGTH_LONG).show();
                        }
                    }else {
                        // un-selected a selected slot
                        wrapperTimeSlot.setSelected(false);
                        toolbarVM.setConfirmedTimeslot(null);
                    }

                    draggableTimeSlotView.updateViewStatus();
                    break;
                }
                case INVITEE_CONFIRM:
                    WrapperTimeSlot wrapperTimeSlot = draggableTimeSlotView.getWrapper();
                    boolean isSelected = !wrapperTimeSlot.isSelected();

                    if (isSelected){
                        if (draggableTimeSlotView.getWrapper().isConflict()){
                            Toast.makeText(getContext(), R.string.timeslot_alert_conflict_select, Toast.LENGTH_SHORT).show();
                        }

                        if (selectedTimeslotsMap.values().size() < selectMax){
                            wrapperTimeSlot.setSelected(true);
                            selectedTimeslotsMap.put(timeSlot.getTimeslotUid(),timeSlot);
                        }else {
                            Toast.makeText(getContext()
                                    , String.format(getContext().getString(R.string.timeslot_alert_create_max_count), selectMax)
                                    , Toast.LENGTH_LONG).show();
                        }
                    }else {
                        // un-selected a selected slot
                        wrapperTimeSlot.setSelected(false);
                        selectedTimeslotsMap.remove(timeSlot.getTimeslotUid());
                    }
                    draggableTimeSlotView.updateViewStatus();
                    updateToolbarTimeslot();
                    break;
            }
        }


        @Override
        public void onRcdTimeSlotClick(RcdRegularTimeSlotView v) {
            v.getWrapper().setSelected(true);
            TimeSlot newSlot = new TimeSlot();
            newSlot.setStartTime(v.getWrapper().getTimeSlot().getStartTime());
            newSlot.setEndTime(v.getWrapper().getTimeSlot().getEndTime());
            timeSlotView.addTimeSlot(newSlot);

            selectedTimeslotsMap.put(newSlot.getTimeslotUid(),newSlot);
            updateToolbarTimeslot();
        }

        @Override
        public void onTimeSlotDragStart(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotDragging(DraggableTimeSlotView draggableTimeSlotView, MyCalendar myCalendar, int i, int i1, int viewX, int viewY, String s) {

        }

        @Override
        public void onTimeSlotDragDrop(DraggableTimeSlotView draggableTimeSlotView, long startTime, long endTime) {
            if (EventUtil.isExpired(draggableTimeSlotView.getNewStartTime())){
                Toast.makeText(getContext(), R.string.timeslot_alert_outdated_create, Toast.LENGTH_LONG).show();
            }else {
                draggableTimeSlotView.getTimeslot().setStartTime(startTime);
                draggableTimeSlotView.getTimeslot().setEndTime(endTime);
            }
        }

        @Override
        public void onTimeSlotDragEnd(DraggableTimeSlotView draggableTimeSlotView) {

        }

        @Override
        public void onTimeSlotEdit(DraggableTimeSlotView draggableTimeSlotView) {
            draggableTimeSlotView.getTimeslot().setStartTime(draggableTimeSlotView.getNewStartTime());
            draggableTimeSlotView.getTimeslot().setEndTime(draggableTimeSlotView.getNewEndTime());
            timeSlotView.refresh();
        }

        @Override
        public void onTimeSlotDelete(DraggableTimeSlotView draggableTimeSlotView) {
            timeSlotView.removeTimeslot(draggableTimeSlotView.getWrapper());

            selectedTimeslotsMap.remove(draggableTimeSlotView.getTimeslot().getTimeslotUid());
            updateToolbarTimeslot();
        }

        @Override
        public void onDateChanged(Date date) {
//            presenter.fetchRecommendedTimeslots(event, date);
        }
    };

    private List<TimeSlotView.DurationItem> initList(){
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
