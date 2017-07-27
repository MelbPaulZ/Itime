package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddContactMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.search.ContactInfoViewModel;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by 37925 on 2016/12/13.
 */

public class ContactHomePageViewModel extends BaseObservable {
    private ContactPresenter presenter;
    private ObservableList<UserInfoViewModel> contacts = new ObservableArrayList<>();
    private int requestCount=0;
    private MainContactsMvpView mvpView;

    public MainContactsMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(MainContactsMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public void loadData(){
        List<Contact> contactList = presenter.getContacts();
        generateContactVM(contactList);
    }

    public View.OnClickListener onSearchClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.gotoSearch();
            }
        };
    }

    private void generateContactVM(List<Contact> contactList){
        contacts.clear();
        Map<String, UserInfoViewModel<Contact>> contactMap = new HashMap<>();
        for(Contact contact: contactList){
            UserInfoViewModel<Contact> vm = new UserInfoViewModel<>();
            vm.setData(contact);
            contacts.add(vm);
            contactMap.put(contact.getUserUid(), vm);
        }
    }

    public OnRecyclerItemClickListener.OnItemClickListener getOnItemClickListener(){
        return new OnRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInfoViewModel<Contact> contact = contacts.get(position);
                mvpView.goToProfileFragment(view, contact.getData());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                onItemClick(view, position);
            }
        };
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.viewModel, R.layout.listview_user_item);
    }

    @Bindable
    public ObservableList<UserInfoViewModel> getContacts() {
        return contacts;
    }

    public void setContacts(ObservableList<UserInfoViewModel> contacts) {
        this.contacts = contacts;
        notifyPropertyChanged(BR.contacts);
    }

    public ContactPresenter getPresenter() {
        return presenter;
    }

    public ContactHomePageViewModel(ContactPresenter presenter){
        this.presenter = presenter;
    }

    public void setPresenter(ContactPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public int getRequestCount(){
        return requestCount;
    }

    public void setRequestCount(int count){
        requestCount = count;
        notifyPropertyChanged(BR.requestCount);
    }

}
