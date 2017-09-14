package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateCalendarsBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateCalendarsMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateCalendarsViewModel;

/**
 * Created by Paul on 7/6/17.
 */

public class FragmentEventCalendar extends ItimeBaseFragment<EventCreateCalendarsMvpView, LocalPresenter<EventCreateCalendarsMvpView>> implements ToolbarInterface{

    private FragmentEventCreateCalendarsBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventCreateCalendarsViewModel vm;
    private Event event;


    @Override
    public LocalPresenter<EventCreateCalendarsMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_calendars, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new EventCreateCalendarsViewModel(getPresenter());
        vm.setEvent(event);
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.toolbar_calendars));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        toolbarViewModel.setRightEnable(true);
        binding.setToolbarVM(toolbarViewModel);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onNext() {
        Fragment fragment = getFrom();
        if (fragment instanceof FragmentEventCreate){
            ((FragmentEventCreate) fragment).setEvent(event);
        }else if (fragment instanceof FragmentEventPrivateCreate){
            ((FragmentEventPrivateCreate) fragment).setEvent(event);
        }else if (fragment instanceof FragmentEventEdit){
            ((FragmentEventEdit)fragment).setEvent(event);
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }
}
