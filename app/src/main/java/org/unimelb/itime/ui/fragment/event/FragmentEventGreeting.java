package org.unimelb.itime.ui.fragment.event;

import android.content.Intent;
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
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.event.EventGreetingMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventGreetingViewModel;

/**
 * Created by Paul on 21/6/17.
 */

public class FragmentEventGreeting extends ItimeBaseFragment<EventGreetingMvpView, EventCreatePresenter<EventGreetingMvpView>>
        implements ToolbarInterface, EventGreetingMvpView {
    private FragmentEventGreetingBinding binding;
    private EventGreetingViewModel vm;
    private ToolbarViewModel toolbarVM;
    private Event event;


    @Override
    public EventCreatePresenter<EventGreetingMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
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
        toolbarVM.setRightEnable(true);
        binding.setToolbarVM(toolbarVM);
    }

    @Override
    public void onNext() {
        event.setEventType(Event.TYPE_GROUP);
        presenter.createEvent(event);
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTaskStart(int taskId) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        if (taskId == EventCreatePresenter.TASK_EVENT_CREATE){
            getActivity().finish();
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
