package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventGreetingBinding;
import org.unimelb.itime.ui.mvpview.event.EventGreetingMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventGreetingViewModel;

/**
 * Created by Paul on 21/6/17.
 */

public class FragmentEventGreeting extends ItimeBaseFragment<EventGreetingMvpView, LocalPresenter<EventGreetingMvpView>> implements ToolbarInterface {
    private FragmentEventGreetingBinding binding;
    private EventGreetingViewModel vm;
    private ToolbarViewModel toolbarVM;
    private Event event;


    @Override
    public LocalPresenter<EventGreetingMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_greeting, container, false);
        }
        return binding.getRoot();
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventGreetingViewModel(getPresenter());
        vm.setEvent(event);
        binding.setVm(vm);

        toolbarVM = new ToolbarViewModel<>(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setTitle(getString(R.string.toolbar_greeting));
        toolbarVM.setRightText(getString(R.string.toolbar_send));
        binding.setToolbarVM(toolbarVM);
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }
}
