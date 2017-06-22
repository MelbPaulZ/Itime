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
import org.unimelb.itime.databinding.FragmentEventRepeatBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.event.EventRepeatMvpView;
import org.unimelb.itime.ui.presenter.EventRepeatPresenter;
import org.unimelb.itime.ui.viewmodel.event.EventRepeatViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 2/6/17.
 */

public class FragmentEventRepeat extends ItimeBaseFragment<EventRepeatMvpView, EventRepeatPresenter<EventRepeatMvpView>> implements EventRepeatMvpView,ToolbarInterface{
    private FragmentEventRepeatBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventRepeatViewModel vm;
    private Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_repeat, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventRepeatViewModel(getPresenter());
        vm.setEvent(event);
        toolbarViewModel = new ToolbarViewModel<>(this);

        toolbarViewModel.setTitle(getString(R.string.event_repeat_title));
        toolbarViewModel.setRightText(getString(R.string.event_repeat_toolbar_done));
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));

        binding.setVm(vm);
        binding.setToolbarvm(toolbarViewModel);

    }

    public void setEvent(Event event){
        this.event = event;
    }

    @Override
    public EventRepeatPresenter<EventRepeatMvpView> createPresenter() {
        return new EventRepeatPresenter<>(getContext());
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

    @Override
    public void toCustomRepeat(Event event) {
        FragmentEventRepeatCustom fragmentEventRepeatCustom = new FragmentEventRepeatCustom();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragmentEventRepeatCustom.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragmentEventRepeatCustom);
    }

    @Override
    public void toEndRepeat(Event event) {
        FragmentEventEndRepeat eventEndRepeat = new FragmentEventEndRepeat();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        eventEndRepeat.setEvent(cpyEvent);
        getBaseActivity().openFragment(eventEndRepeat);

    }
}
