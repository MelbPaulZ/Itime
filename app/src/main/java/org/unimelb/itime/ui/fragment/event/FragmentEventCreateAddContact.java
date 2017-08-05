package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentEventCreateAddContactsBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddContactMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateAddContactViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qiushuo Huang on 2017/6/1.
 */

public class FragmentEventCreateAddContact extends ItimeBaseFragment<EventCreateAddContactMvpView, ContactPresenter<EventCreateAddContactMvpView>>
        implements EventCreateAddContactMvpView, ToolbarInterface{

    private FragmentEventCreateAddContactsBinding binding;
    private EventCreateAddContactViewModel viewModel;
    private ToolbarViewModel toolbarViewModel;
    private List<Invitee> invitees;
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_event_create_add_contacts, container, false);

        return binding.getRoot();
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new EventCreateAddContactViewModel(getPresenter());
        viewModel.setInvitees(invitees);
        viewModel.setEvent(event);
        binding.setContentVM(viewModel);
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.event_create_itimecontacts));
        toolbarViewModel.setRightEnable(true);
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        binding.setToolbarVM(toolbarViewModel);
    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
        presenter.loadContacts();
    }

    public void goToProfileFragment(View view, Contact user) {

    }

    @Override
    public ContactPresenter<EventCreateAddContactMvpView> createPresenter() {
        return new ContactPresenter<>(getContext());
    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        invitees.clear();
        invitees.addAll(viewModel.getSelectedInvitee());
        onBack();
    }

    private Map<String, Invitee> getInviteeMap(List<Invitee> list){
        HashMap<String, Invitee> result = new HashMap<>();
        if(list == null){
            return result;
        }
        for(Invitee invitee:list){
            result.put(invitee.getInviteeUid(), invitee);
        }
        return result;
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            case ContactPresenter.TAST_ALL_CONTACT:
                if(data instanceof List)
                    viewModel.loadData((List<Contact>) data);
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
