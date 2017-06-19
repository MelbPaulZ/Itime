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
import org.unimelb.itime.databinding.FragmentEventCreateTitleBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateTitleMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTitlePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateTitleViewModel;

/**
 * Created by Paul on 9/6/17.
 */

public class FragmentEventCreateTitle extends ItimeBaseFragment<EventCreateTitleMvpView, EventCreateTitlePresenter<EventCreateTitleMvpView>>
        implements ToolbarInterface {

    private FragmentEventCreateTitleBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventCreateTitleViewModel vm;
    private Event event;


    @Override
    public EventCreateTitlePresenter<EventCreateTitleMvpView> createPresenter() {
        return new EventCreateTitlePresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_title, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventCreateTitleViewModel(getPresenter());
        vm.setEvent(event);
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.toolbar_event));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        binding.setToolbarVM(toolbarViewModel);

    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onNext() {

        Fragment fragment = getFrom();
        if (fragment instanceof FragmentEventCreate){
            ((FragmentEventCreate)fragment).setEvent(event);
        }else if (fragment instanceof FragmentEventPrivateCreate){
            ((FragmentEventPrivateCreate)fragment).setEvent(event);
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }
}
