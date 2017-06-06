package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.databinding.FragmentEventEndRepeatBinding;
import org.unimelb.itime.ui.mvpview.event.EventEndRepeatMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.event.EventEndRepeatViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.Calendar;

/**
 * Created by Paul on 5/6/17.
 */

public class FragmentEventEndRepeat extends ItimeBaseFragment<EventEndRepeatMvpView, LocalPresenter<EventEndRepeatMvpView>> {
    private FragmentEventEndRepeatBinding binding;
    private EventEndRepeatViewModel vm;
    private ToolbarViewModel toolbarViewModel;

    @Override
    public LocalPresenter<EventEndRepeatMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new EventEndRepeatViewModel(getPresenter());
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel();
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.event_end_repeat));
        toolbarViewModel.setRightText(getString(R.string.event_repeat_toolbar_done));
        binding.setToolbarVM(toolbarViewModel);

        final DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.date_picker);
        Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                vm.setNeverCheckVisibility(View.GONE);
                vm.setOnDateCheckVisibility(View.VISIBLE);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_end_repeat, container, false);
        return binding.getRoot();
    }
}
