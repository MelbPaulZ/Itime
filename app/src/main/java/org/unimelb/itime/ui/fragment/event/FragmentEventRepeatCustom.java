package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.databinding.FragmentEventRepeatCustomBinding;
import org.unimelb.itime.ui.mvpview.event.EventRepeatCustomMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.event.EventRepeatCustomViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 5/6/17.
 */

public class FragmentEventRepeatCustom extends ItimeBaseFragment<EventRepeatCustomMvpView, LocalPresenter<EventRepeatCustomMvpView>> {
    private FragmentEventRepeatCustomBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventRepeatCustomViewModel vm;

    @Override
    public LocalPresenter<EventRepeatCustomMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_repeat_custom, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventRepeatCustomViewModel(getPresenter());
        vm.setFrequencyString(getWheelData().get(0));
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel();
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.event_repeat_custom));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        binding.setToolbarVM(toolbarViewModel);

        initWheel();

    }


    private void initWheel(){
        WheelPicker wheelPicker = (WheelPicker) getActivity().findViewById(R.id.repeat_wheel_picker);
        wheelPicker.setData(getWheelData());
        wheelPicker.setSelectedItemTextColor(getResources().getColor(R.color.azure));
        wheelPicker.setItemTextSize(SizeUtil.dip2px(getContext(), 20));
        wheelPicker.setVisibleItemCount(5);
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                vm.setFrequencyString(getWheelData().get(position));
            }
        });

    }

    private List<String> getWheelData(){
        List<String> strings = new ArrayList<>();
        strings.add(getString(R.string.event_custom_wheel_daily));
        strings.add(getString(R.string.event_custom_wheel_weekly));
        strings.add(getString(R.string.event_custom_wheel_monthly));
        strings.add(getString(R.string.event_custom_wheel_annually));
        return strings;
    }

}
