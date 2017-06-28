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
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventDetailConfirmBinding;
import org.unimelb.itime.ui.mvpview.event.EventDetailConfirmMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventDetailConfirmViewModel;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/26.
 */

public class FragmentEventDetailConfirm extends ItimeBaseFragment<EventDetailConfirmMvpView, EventCreatePresenter<EventDetailConfirmMvpView>>
        implements EventDetailConfirmMvpView , ToolbarInterface {
    private EventDetailConfirmViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;
    private Event event;
    private TimeSlot timeSlot;
    private FragmentEventDetailConfirmBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_confirm, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new EventDetailConfirmViewModel(getPresenter());
        contentViewModel.setEvent(event);
        contentViewModel.setTimeSlot(timeSlot);
        contentViewModel.setMvpView(this);
        binding.setVm(contentViewModel);
        toolbarViewModel = new ToolbarViewModel<>(this);

        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_close));
        toolbarViewModel.setTitle(getString(R.string.event_create_confirm_toolbar));
        binding.setToolbarVM(toolbarViewModel);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public EventCreatePresenter<EventDetailConfirmMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Override
    public void confirm() {
        presenter.confirmEvent(event);
    }

    @Override
    public void changeAlert() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {
        switch (taskId){
            case EventCreatePresenter.TASK_EVENT_CONFIRM:
                getBaseActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }
}
