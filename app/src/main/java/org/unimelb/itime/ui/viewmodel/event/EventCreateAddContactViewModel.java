package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddContactMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;
import org.unimelb.itime.widget.listview.UserInfoViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/6/22.
 */

public class EventCreateAddContactViewModel extends BaseObservable{
    private EventCreatePresenter presenter;
    private Context context;
    private EventCreateAddContactMvpView mvpView;
    private List<Invitee> invitees = new ArrayList<>();
    private ObservableList<UserInfoViewModel> contacts = new ObservableArrayList<>();
    private List<Contact> selectedList = new ArrayList<>();
    private ToolbarViewModel toolbarViewModel;
    private Event event;

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    public void loadData(){
        List<Contact> contactList = presenter.getContacts();
        generateContactVM(contactList);
    }

    private void generateContactVM(List<Contact> contactList){
        contacts.clear();
        selectedList.clear();
        Map<String, UserInfoViewModel<Contact>> contactMap = new HashMap<>();
        for(Contact contact: contactList){
            UserInfoViewModel<Contact> vm = new UserInfoViewModel<>();
            vm.setData(contact);
            contacts.add(vm);
            contactMap.put(contact.getUserUid(), vm);
        }

        for(Invitee invitee:invitees){
            if(contactMap.get(invitee.getUserUid())!=null){
                contactMap.get(invitee.getUserUid()).setSelect(true);
                selectedList.add(contactMap.get(invitee.getUserUid()).getData());
            }
        }
    }

    public OnRecyclerItemClickListener.OnItemClickListener getOnItemClickListener(){
        return new OnRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInfoViewModel<Contact> contact = contacts.get(position);
                if(selectedList.contains(contact.getData())){
                    selectedList.remove(contact.getData());
                    contact.setSelect(false);
                }else{
                    selectedList.add(contact.getData());
                    contact.setSelect(true);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                onItemClick(view, position);
            }
        };
    }

    private Invitee contactToInvitee(Contact contact){

        return EventUtil.generateInvitee(event, contact);
    }

    public ToolbarViewModel getToolbarViewModel() {
        return toolbarViewModel;
    }

    public void setToolbarViewModel(ToolbarViewModel toolbarViewModel) {
        this.toolbarViewModel = toolbarViewModel;
    }

    public EventCreateAddContactViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
    }

    public EventCreatePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(EventCreatePresenter presenter) {
        this.presenter = presenter;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EventCreateAddContactMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(EventCreateAddContactMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public List<Invitee> getSelectedInvitee(){
        List<Invitee> invitees = new ArrayList<>();
        for(Contact contact:selectedList){
            invitees.add(contactToInvitee(contact));
        }
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.viewModel, R.layout.listview_selectable_user_item);
    }

    @Bindable
    public ObservableList<UserInfoViewModel> getContacts() {
        return contacts;
    }

    public void setContacts(ObservableList<UserInfoViewModel> contacts) {
        this.contacts = contacts;
        notifyPropertyChanged(BR.contacts);
    }
}
