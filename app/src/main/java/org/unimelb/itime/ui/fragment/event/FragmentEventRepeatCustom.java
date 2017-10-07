package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventRepeatCustomBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.event.EventRepeatCustomMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.event.EventRepeatCustomViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.SizeUtil;
import org.unimelb.itime.util.rulefactory.FrequencyEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 5/6/17.
 */

public class FragmentEventRepeatCustom extends ItimeBaseFragment<EventRepeatCustomMvpView, LocalPresenter<EventRepeatCustomMvpView>>
        implements ToolbarInterface, EventRepeatCustomMvpView{
    private FragmentEventRepeatCustomBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventRepeatCustomViewModel vm;
    private Event event;
    private WheelPicker wheelPicker, freqWheelPicker;
    private List<String> dayStrings, weekStrings, monthStrings, yearStrings, singRepeats, pluralRepeats;

    @Override
    public LocalPresenter<EventRepeatCustomMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_repeat_custom, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (vm==null) {
            initWheel();

            vm = new EventRepeatCustomViewModel(getPresenter());
            vm.setGapString((String) freqWheelPicker.getData().get(0));
            vm.setFrequencyString((String) wheelPicker.getData().get(0));
            vm.setEvent(event);
            binding.setVm(vm);

            toolbarViewModel = new ToolbarViewModel<>(this);
            toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
            toolbarViewModel.setTitle(getString(R.string.event_repeat_custom));
            toolbarViewModel.setRightText(getString(R.string.toolbar_done));
            toolbarViewModel.setRightEnable(true);
            binding.setToolbarVM(toolbarViewModel);

        }
    }


    private void initWheel(){
        // day, week, month ..
        wheelPicker = (WheelPicker) getActivity().findViewById(R.id.repeat_wheel_picker);
        wheelPicker.setData(getRepeatSingle());
        wheelPicker.setSelectedItemTextColor(getResources().getColor(R.color.azure));
        wheelPicker.setItemTextSize(SizeUtil.dip2px(getContext(), 16));
        wheelPicker.setVisibleItemCount(5);
        wheelPicker.setIndicatorSize(2);
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(getResources().getColor(R.color.divider_line));
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                if (position == 0){
                    freqWheelPicker.setData(getWheelDays());
                }

                if (position == 1){
                    freqWheelPicker.setData(getWheelWeeks());
                }

                if (position == 2){
                    freqWheelPicker.setData(getWheelMonths());
                }

                if (position == 3){
                    freqWheelPicker.setData(getWheelYears());
                }
                // update title
                vm.setFrequencyString((String) wheelPicker.getData().get(position));
                vm.setGapString((String) freqWheelPicker.getData().get(freqWheelPicker.getSelectedItemPosition()));
                updateRepeatType(position);
            }
        });


        // 1,2,3,4,5....
        freqWheelPicker = (WheelPicker) getActivity().findViewById(R.id.repeat_wheel_picker_freq);
        freqWheelPicker.setData(getWheelDays());
        freqWheelPicker.setSelectedItemTextColor(getResources().getColor(R.color.azure));
        freqWheelPicker.setItemTextSize(SizeUtil.dip2px(getContext(), 16));
        freqWheelPicker.setVisibleItemCount(5);
        freqWheelPicker.setIndicatorSize(2);
        freqWheelPicker.setIndicator(true);
        freqWheelPicker.setIndicatorColor(getResources().getColor(R.color.divider_line));
        freqWheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                if (position==0){
                    wheelPicker.setData(getRepeatSingle());
                }else{
                    if (wheelPicker.getData().equals(getRepeatSingle())){
                        wheelPicker.setData(getRepeatPlural());
                    }
                }
                vm.setGapString((String) freqWheelPicker.getData().get(position));
                vm.setFrequencyString((String) wheelPicker.getData().get(wheelPicker.getSelectedItemPosition()));
                updateInterval(position);

            }
        });

        // default set value
        event.getRule().setFrequencyEnum(FrequencyEnum.DAILY);
        event.getRule().setInterval(1);
        event.setRecurrence(event.getRule().getRecurrence());

    }

    private void updateRepeatType(int position){
        if (position == 0){
            event.getRule().setFrequencyEnum(FrequencyEnum.DAILY);
        }
        if (position == 1){
            event.getRule().setFrequencyEnum(FrequencyEnum.WEEKLY);
        }
        if (position == 2){
            event.getRule().setFrequencyEnum(FrequencyEnum.MONTHLY);
        }
        if (position == 3){
            event.getRule().setFrequencyEnum(FrequencyEnum.YEARLY);
        }

        event.setRecurrence(event.getRule().getRecurrence());

    }

    private void updateInterval(int position){
        // give a default value for FrequencyEnum
        if (event.getRule().getFrequencyEnum()==null){
            event.getRule().setFrequencyEnum(FrequencyEnum.DAILY);
        }
        event.getRule().setInterval(position+1);
        event.setRecurrence(event.getRule().getRecurrence());
    }

    private List<String> getRepeatSingle(){
        if (singRepeats==null) {
            singRepeats = new ArrayList<>();
            singRepeats.add(getString(R.string.event_custom_repeat_day));
            singRepeats.add(getString(R.string.event_custom_repeat_week));
            singRepeats.add(getString(R.string.event_custom_repeat_month));
            singRepeats.add(getString(R.string.event_custom_wheel_year));
        }
        return singRepeats;
    }

    private List<String> getRepeatPlural(){
        if (pluralRepeats == null){
            pluralRepeats = new ArrayList<>();
            pluralRepeats.add(getString(R.string.event_custom_wheel_days));
            pluralRepeats.add(getString(R.string.event_custom_wheel_weeks));
            pluralRepeats.add(getString(R.string.event_custom_wheel_months));
            pluralRepeats.add(getString(R.string.event_custom_wheel_years));
        }
        return pluralRepeats;
    }

    private List<String> getWheelDays(){
        if (dayStrings==null) {
            dayStrings = new ArrayList<>();
            for (int i = 1; i <= 99; i++) {
                dayStrings.add(i + "");
            }
        }
        return dayStrings;
    }

    private List<String> getWheelWeeks(){
        if (weekStrings==null) {
            weekStrings = new ArrayList<>();
            for (int i = 1; i <= 52; i++) {
                weekStrings.add(i + "");
            }
        }
        return weekStrings;
    }

    private List<String> getWheelMonths(){
        if (monthStrings==null){
            monthStrings = new ArrayList<>();
            for (int i = 1 ; i <= 36; i++){
                monthStrings.add(i + "");
            }
        }
        return monthStrings;
    }

    private List<String> getWheelYears(){
        if (yearStrings == null){
            yearStrings = new ArrayList<>();
            for (int i = 1; i <= 30; i++){
                yearStrings.add(i + "");
            }
        }
        return yearStrings;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onNext() {
        Fragment fragment = getTargetFragment();
        if (fragment instanceof FragmentEventCreate){
            ((FragmentEventCreate) fragment).setEvent(event);
            getFragmentManager().popBackStack(FragmentEventCreate.class.getSimpleName(), 0);
        }else if (fragment instanceof FragmentEventPrivateCreate){
            ((FragmentEventPrivateCreate) fragment).setEvent(event);
            getFragmentManager().popBackStack(FragmentEventPrivateCreate.class.getSimpleName(), 0 );
        }else if (fragment instanceof FragmentEventEdit){
            ((FragmentEventEdit) fragment).setEvent(event);
            getFragmentManager().popBackStack(FragmentEventEdit.class.getSimpleName(), 0 );
        }
        getBaseActivity().backFragmentBottomUp(fragment);
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void toEndRepeat(Event event) {
        FragmentEventEndRepeat eventEndRepeat = new FragmentEventEndRepeat();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        eventEndRepeat.setEvent(cpyEvent);
        getBaseActivity().openFragment(eventEndRepeat);
    }
}
