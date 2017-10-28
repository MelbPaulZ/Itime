package org.unimelb.itime.ui.fragment.component;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.ITimeTimeslotCalendar;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventTimeBinding;
import org.unimelb.itime.ui.mvpview.ItimeDialogShowChangeInterface;
import org.unimelb.itime.ui.mvpview.component.Cancellable;
import org.unimelb.itime.ui.viewmodel.component.EventTimeViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.SizeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Paul on 19/6/17.
 */

public class FragmentEventTime extends DialogFragment implements Cancellable, ItimeDialogShowChangeInterface{

    private FragmentEventTimeBinding binding;
    private EventTimeViewModel vm;
    private WheelPicker hourPicker, minutePicker;
    private Event event;
    private String TAG = "FragmentEventTime";
    private Calendar curStartCalendar, curEndCalendar;
    private ItimeDialogSaveCallBack itimeDialogSaveCallBack;
    private ITimeTimeslotCalendar datePicker;
    private int firstShowStartOrEnd;
    private boolean changeEndTime = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_time, container, false);

        setDialogPosition();

        getDialog().setCanceledOnTouchOutside(true);
        return binding.getRoot();
    }

    private void setDialogPosition(){
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        // need to set a background so this window can show properly
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.triangle));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventTimeViewModel(getActivity(),this);
        vm.setEvent(event);
        vm.setSelectDate(EventUtil.parseTimeZoneToDate(event.getStart().getDateTime()));
        binding.setVm(vm);

        curStartCalendar = Calendar.getInstance();
        curEndCalendar = Calendar.getInstance();
        String startDateTime = event.getStart().getDateTime();
        curStartCalendar.setTime(EventUtil.parseTimeZoneToDate(startDateTime));
        curEndCalendar.setTime(EventUtil.parseTimeZoneToDate(event.getEnd().getDateTime()));
        datePicker = (ITimeTimeslotCalendar) binding.getRoot().findViewById(R.id.compactcalendar_view);

        datePicker.setBodyListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                if (vm.getSelectTime() == EventTimeViewModel.EVENT_TIME_START_TIME){
                    curStartCalendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    if (!changeEndTime){
                        curEndCalendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), curStartCalendar.get(Calendar.HOUR_OF_DAY), curStartCalendar.get(Calendar.MINUTE));
                        curEndCalendar.roll(Calendar.HOUR_OF_DAY, 1);
                    }
                }else if (vm.getSelectTime() == EventTimeViewModel.EVENT_TIME_END_TIME){
                    curEndCalendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    changeEndTime = true;
                }
                vm.setSelectDate(date);
            }

            @Override
            public void onMonthScroll(Date date) {
                vm.setSelectDate(date);
            }
        });



        hourPicker = (WheelPicker) binding.getRoot().findViewById(R.id.event_hour_wheel_picker);
        hourPicker.setSelectedItemTextColor(getResources().getColor(R.color.azure));
        hourPicker.setItemTextSize(SizeUtil.dip2px(getActivity(), 16));
        hourPicker.setVisibleItemCount(5);
        hourPicker.setData(getHours());
        hourPicker.setCyclic(true);

        hourPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                if (vm.getSelectTime() == EventTimeViewModel.EVENT_TIME_START_TIME){
                    curStartCalendar.set(Calendar.HOUR_OF_DAY, position);
                    if (!changeEndTime){
                        curEndCalendar.set(Calendar.HOUR_OF_DAY,position);
                        curEndCalendar.roll(Calendar.HOUR_OF_DAY, 1);
                    }
                }else if (vm.getSelectTime() == EventTimeViewModel.EVENT_TIME_END_TIME){
                    curEndCalendar.set(Calendar.HOUR_OF_DAY, position);
                    changeEndTime = true;
                }
            }
        });


        minutePicker = (WheelPicker) binding.getRoot().findViewById(R.id.event_minute_wheel_picker);
        minutePicker.setSelectedItemTextColor(getResources().getColor(R.color.azure));
        minutePicker.setItemTextSize(SizeUtil.dip2px(getActivity(), 16));
        minutePicker.setVisibleItemCount(5);
        minutePicker.setData(getMinutes());
        minutePicker.setCyclic(true);
        minutePicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                if (vm.getSelectTime() == EventTimeViewModel.EVENT_TIME_START_TIME){
                    curStartCalendar.set(Calendar.MINUTE, position);
                    if (!changeEndTime){
                        curEndCalendar.set(Calendar.MINUTE, position);
                    }
                }else if (vm.getSelectTime() == EventTimeViewModel.EVENT_TIME_END_TIME){
                    curEndCalendar.set(Calendar.MINUTE, position);
                    changeEndTime = true;
                }
            }
        });

        updateShowingCalendar(firstShowStartOrEnd);
        if (!changeEndTime && firstShowStartOrEnd == EventTimeViewModel.EVENT_TIME_START_TIME){
            updateShowingCalendar(EventTimeViewModel.EVENT_TIME_END_TIME);
            updateShowingCalendar(EventTimeViewModel.EVENT_TIME_START_TIME); // fix bug when click again but end time display
        }
        vm.setSelectTime(firstShowStartOrEnd);
    }

    public void onBackCalendar(){
        datePicker.getCalendarView().showPreviousMonth();
    }

    public void onNextCalendar(){
        datePicker.getCalendarView().showNextMonth();
    }

    // TODO: 24/6/17 calendar show, david add
    private void updateShowingCalendar(int selectTime){
        if (selectTime == EventTimeViewModel.EVENT_TIME_START_TIME){
            int hour = curStartCalendar.get(Calendar.HOUR_OF_DAY);
            hourPicker.setSelectedItemPosition(hour);
            minutePicker.setSelectedItemPosition(curStartCalendar.get(Calendar.MINUTE));
        }else if (selectTime == EventTimeViewModel.EVENT_TIME_END_TIME){
            hourPicker.setSelectedItemPosition(curEndCalendar.get(Calendar.HOUR_OF_DAY));
            minutePicker.setSelectedItemPosition(curEndCalendar.get(Calendar.MINUTE));
        }
    }

    private List<String> getHours(){
        List<String> hourList = new ArrayList<>();
        for (int i = 0 ; i < 24; i++){
            if (i<10){
                hourList.add("0" + i);
            }else {
                hourList.add(i + "");
            }
        }
        return hourList;
    }

    private List<String> getMinutes(){
        List<String> minutes = new ArrayList<>();
        for ( int i = 0 ; i < 60 ; i ++){
            if (i < 10){
                minutes.add("0" + i);
            }else{
                minutes.add(i + "");
            }
        }
        return minutes;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setFirstShowStartOrEnd(int firstShowStartOrEnd) {
        this.firstShowStartOrEnd = firstShowStartOrEnd;
    }

    @Override
    public void cancel() {

        dismiss();
    }

    private boolean checkTimeValidation(){
        return curStartCalendar.getTimeInMillis()/(1000*60)< curEndCalendar.getTimeInMillis()/(1000*60);
    }

    @Override
    public void save() {
        if (!checkTimeValidation()){
            Toast.makeText(getContext(), "End Time Cannot before start time", Toast.LENGTH_SHORT).show();
            return;
        }
        event.getStart().setDateTime(EventUtil.getFormatTimeString(curStartCalendar.getTimeInMillis(), EventUtil.TIME_ZONE_PATTERN));
        event.getEnd().setDateTime(EventUtil.getFormatTimeString(curEndCalendar.getTimeInMillis(), EventUtil.TIME_ZONE_PATTERN));
        if (itimeDialogSaveCallBack!=null){
            itimeDialogSaveCallBack.onSave(event);
        }
        dismiss();
    }

    public void setItimeDialogSaveCallBack(ItimeDialogSaveCallBack itimeDialogSaveCallBack) {
        this.itimeDialogSaveCallBack = itimeDialogSaveCallBack;
    }

    @Override
    public void onShowPage(int startOrEnd) {
        updateShowingCalendar(startOrEnd);
    }

    public interface ItimeDialogSaveCallBack{
        void onSave(Event event);
    }


}
