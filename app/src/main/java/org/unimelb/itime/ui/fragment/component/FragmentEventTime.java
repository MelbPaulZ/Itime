package org.unimelb.itime.ui.fragment.component;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.aigestudio.wheelpicker.WheelPicker;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.ITimeTimeslotCalendar;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentEventTimeBinding;
import org.unimelb.itime.ui.mvpview.component.Cancellable;
import org.unimelb.itime.ui.viewmodel.component.EventTimeViewModel;
import org.unimelb.itime.util.SizeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Paul on 19/6/17.
 */

public class FragmentEventTime extends DialogFragment implements Cancellable{

    private FragmentEventTimeBinding binding;
    private EventTimeViewModel vm;
    private WheelPicker hourPicker, minutePicker;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_time, container, false);
        }

        setDialogPosition();

        getDialog().setCanceledOnTouchOutside(true);
        return binding.getRoot();
    }

    private void setDialogPosition(){
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.LEFT);

//        WindowManager.LayoutParams params = window.getAttributes();
//        params.y = SizeUtil.dip2px(getContext(),60);
//        window.setAttributes(params);

        // need to set a background so this window can show properly
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.triangle));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventTimeViewModel(getActivity(),this);
        binding.setVm(vm);

        ITimeTimeslotCalendar datePicker = (ITimeTimeslotCalendar) binding.getRoot().findViewById(R.id.compactcalendar_view);
        datePicker.setBodyListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
                Log.i("asd", "onDayClick: " + date);
            }

            @Override
            public void onMonthScroll(Date date) {

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

            }
        });


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

    @Override
    public void cancel() {
        dismiss();
    }

    @Override
    public void save() {
        dismiss();
    }
}
