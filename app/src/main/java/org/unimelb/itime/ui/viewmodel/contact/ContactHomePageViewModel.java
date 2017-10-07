package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.BaseRecyclerAdapter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddContactMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.search.ContactInfoViewModel;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;
import org.unimelb.itime.widget.listview.UserInfoViewModel;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.util.ArrayList;
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
    private PopupMenu menu;
    private List<PopupMenu.Item> menuItem;
    private PopupMenu.OnItemClickListener onMenuItemClicked;

    public MainContactsMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(MainContactsMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public void loadData(List<Contact> contactList){
        generateContactVM(contactList);
        initMenu();
    }

    private void initMenu(){
        menu = new PopupMenu(presenter.getContext());
        menuItem = new ArrayList<>();
        menuItem.add(new PopupMenu.Item(R.drawable.icon_contacts_addfriends,
                presenter.getContext().getResources().getString(R.string.contact_add_by_email)));
        menuItem.add(new PopupMenu.Item(R.drawable.icon_contacts_scanqr,
                presenter.getContext().getResources().getString(R.string.contact_scan_qr_code)));
        menu.setItems(menuItem);

        onMenuItemClicked = new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(int position, PopupMenu.Item item) {
                if(mvpView==null){
                    return;
                }
                switch (position){
                    case 0:
                        mvpView.goToAddFriendsFragment();
                        break;
                    case 1:
                        mvpView.goToScanQR();
                }
            }
        };
        menu.setOnItemClickListener(onMenuItemClicked);
    }

    public View.OnClickListener onMoreFriendsClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null)
                    mvpView.goToAddFriendsFragment();
            }
        };
    }

    public View.OnClickListener onFriendRequestClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null)
                    mvpView.goToNewFriendFragment();
            }
        };
    }

    public View.OnClickListener onScanQRClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null)
                    mvpView.goToScanQR();
            }
        };
    }

    public View.OnClickListener onPlusClicked(){
       return new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(menu!=null){
                   menu.showLocation(view);
               }
           }
       };
    }

    public View.OnClickListener onSearchClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.gotoSearch();
            }
        };
    }

    public View.OnClickListener onMyProfile(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.toMyProfile();
                }
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

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener(){
        return new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Object data) {
                UserInfoViewModel<Contact> contact = contacts.get(position);
                if(mvpView!=null) {
                    mvpView.goToProfileFragment(null, contact.getData());
                }
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
