package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateBinding;
import org.unimelb.itime.databinding.FragmentEventCreateDurationBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateDurationMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateDurationViewModel;
import org.unimelb.itime.util.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 7/6/17.
 */

public class FragmentEventCreateDuration extends ItimeBaseFragment<EventCreateDurationMvpView, LocalPresenter<EventCreateDurationMvpView>> implements ToolbarInterface{

    private Event event;
    private FragmentEventCreateDurationBinding binding;
    private EventCreateDurationViewModel vm;
    private ToolbarViewModel toolbarViewModel;


    @Override
    public LocalPresenter<EventCreateDurationMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_duration, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventCreateDurationViewModel(getPresenter());
        vm.setDuration(getDurationData().get(3));
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.event_create_duration));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        binding.setToolbarVM(toolbarViewModel);

        WheelPicker durationWheelPicker = (WheelPicker) getActivity().findViewById(R.id.duration_wheel_picker);
        durationWheelPicker.setSelectedItemTextColor(getResources().getColor(R.color.azure));
        durationWheelPicker.setVisibleItemCount(7);
        durationWheelPicker.setData(getDurationData());
        durationWheelPicker.setItemTextSize(SizeUtil.dip2px(getContext(), 16));
        durationWheelPicker.setCyclic(true);
        durationWheelPicker.setSelectedItemPosition(3); // // TODO: 8/6/17 change to follow event
        durationWheelPicker.setIndicator(true);
        durationWheelPicker.setIndicatorColor(getResources().getColor(R.color.divider_line));
        durationWheelPicker.setIndicatorSize(2);
        durationWheelPicker.setPadding(SizeUtil.dip2px(getContext(), 30), 0, SizeUtil.dip2px(getContext(), 30), 0);
        durationWheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                vm.setDuration(getDurationData().get(position));
            }
        });
    }

    private List<String> getDurationData(){
        List<String> dataList = new ArrayList<>();
        dataList.add(getString(R.string.duration_15_minutes));
        dataList.add(getString(R.string.duration_30_minutes));
        dataList.add(getString(R.string.duration_45_minutes));
        dataList.add(getString(R.string.duration_1_hour));
        dataList.add(getString(R.string.duration_2_hours));
        dataList.add(getString(R.string.duration_6_hours));
        dataList.add(getString(R.string.duration_all_day));
        return dataList;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onNext() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }
}
