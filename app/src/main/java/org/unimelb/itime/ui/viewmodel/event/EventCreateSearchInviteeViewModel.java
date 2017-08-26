package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.event.EventCreateSearchInviteeMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/6/23.
 */

public class EventCreateSearchInviteeViewModel extends BaseObservable {
    private boolean searching;
    private EventCreatePresenter presenter;
    private Context context;
    private EventCreateSearchInviteeMvpView mvpView;
    private List<Invitee> invitees = new ArrayList<>();
    private ObservableList<UserInfoViewModel> results = new ObservableArrayList<>();
    private ObservableList<UserInfoViewModel> recent = new ObservableArrayList<>();
    private List<UserInfoViewModel> contacts = new ArrayList<>();
    private String searchStr = "";
    private Event event;


    public View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.goBack();
                }
            }
        };
    }

    public View.OnClickListener onContactClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.gotoAddContact();
                }
            }
        };
    }

    public View.OnClickListener onClearClick(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchStr("");
            }
        };
    }

    @Bindable
    public ObservableList<UserInfoViewModel> getRecent() {
        return recent;
    }

    public void setRecent(ObservableList<UserInfoViewModel> recent) {
        this.recent = recent;
        notifyPropertyChanged(BR.recent);
    }

    public void loadData(){
        List<Contact> contactList = presenter.getContacts();
        List<Contact> recentList = presenter.getRecentContacts();
        generateContactVM(contacts, contactList);
        generateContactVM(recent, recentList);
    }

    private void search(){
        results.clear();
        String tmp = searchStr.toLowerCase();
        for(UserInfoViewModel vm: contacts){
            if(vm.tryMatch(tmp)){
                results.add(vm);
            }
        }
    }

    public TextWatcher onEdit(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(searchStr.isEmpty()){
                    setSearching(false);
                }else {
                    setSearching(true);
                    search();
                }
            }
        };
    }

    @Bindable
    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
        notifyPropertyChanged(BR.searching);
    }

    @Bindable
    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
        if(searchStr.isEmpty()){
            setSearching(false);
        }else{
            setSearching(true);
        }
        notifyPropertyChanged(BR.searchStr);
    }

    private void generateContactVM(List<UserInfoViewModel> result, List<Contact> contactList){
        result.clear();
        Map<String, UserInfoViewModel<Contact>> contactMap = new HashMap<>();
        for(Contact contact: contactList){
            UserInfoViewModel<Contact> vm = new UserInfoViewModel<>();
            vm.setData(contact);
            result.add(vm);
            contactMap.put(contact.getUserUid(), vm);
        }
    }

    public AdapterView.OnItemClickListener getOnSearchItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfoViewModel<Contact> contact = results.get(i);
                boolean contain = false;
                for(Invitee invitee: invitees){
                    if(invitee.getUserUid().equals(contact.getData().getUserDetail().getUserUid())){
                        invitees.remove(invitee);
                        contain=true;
                        break;
                    }
                }
                if(!contain) {
                    invitees.add(0, EventUtil.generateInvitee(event, contact.getData()));
                }
                if(mvpView!=null){
                    mvpView.goBack();
                }
            }
        };
    }

    public AdapterView.OnItemClickListener getOnRecentItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfoViewModel<Contact> contact = recent.get(i);
                boolean contain = false;
                for (Invitee invitee : invitees) {
                    if (invitee.getUserUid().equals(contact.getData().getUserDetail().getUserUid())) {
                        invitees.remove(invitee);
                        contain = true;
                        break;
                    }
                }
                if (!contain) {
                    invitees.add(0, EventUtil.generateInvitee(event, contact.getData()));
                }
                if (mvpView != null) {
                    mvpView.goBack();
                }
            }
        };
    }

    public EventCreateSearchInviteeViewModel(EventCreatePresenter presenter) {
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

    public EventCreateSearchInviteeMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(EventCreateSearchInviteeMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.viewModel, R.layout.listview_user_item);
    }

    public ObservableList<UserInfoViewModel> getResults() {
        return results;
    }

    public void setResults(ObservableList<UserInfoViewModel> results) {
        this.results = results;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
