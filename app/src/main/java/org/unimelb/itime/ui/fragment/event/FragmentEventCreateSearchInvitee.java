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
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentCreateEventAddInviteeBinding;
import org.unimelb.itime.databinding.FragmentEventCreateSearchInviteeBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddInviteeMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateSearchInviteeMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateAddInviteeViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateSearchInviteeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/23.
 */

public class FragmentEventCreateSearchInvitee  extends ItimeBaseFragment<EventCreateSearchInviteeMvpView, EventCreatePresenter<EventCreateSearchInviteeMvpView>>
        implements EventCreateSearchInviteeMvpView {

    private FragmentEventCreateSearchInviteeBinding binding;
    private Event event;
    private EventCreateSearchInviteeViewModel contentVM;
    private List<Invitee> inviteeList = new ArrayList<>();
    private FragmentEventCreateAddContact addContactFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_search_invitee, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentVM = new EventCreateSearchInviteeViewModel(getPresenter());

        event = new Event();
        contentVM.setMvpView(this);
        contentVM.setInvitees(inviteeList);
        contentVM.setContext(getContext());
        contentVM.setEvent(event);

        binding.setVm(contentVM);
    }

    @Override
    public void onResume() {
        super.onResume();
        contentVM.loadData();
    }

    public List<Invitee> getInviteeList() {
        return inviteeList;
    }

    public void setInviteeList(List<Invitee> inviteeList) {
        this.inviteeList = inviteeList;
    }

    @Override
    public EventCreatePresenter createPresenter() {
        return new EventCreatePresenter(getContext());
    }

    @Override
    public void goBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void gotoAddContact() {
        if(addContactFragment==null){
            addContactFragment = new FragmentEventCreateAddContact();
        }
        addContactFragment.setInvitees(contentVM.getInvitees());
        addContactFragment.setEvent(contentVM.getEvent());
        getBaseActivity().openFragment(addContactFragment, null, false);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}