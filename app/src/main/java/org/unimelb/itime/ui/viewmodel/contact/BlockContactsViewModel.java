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
import org.unimelb.itime.ui.mvpview.contact.BlockContactsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class BlockContactsViewModel extends BaseObservable {
    private ContactPresenter presenter;
    private boolean showTitileBack = true;
    private boolean showTitleRight = false;
    private ObservableList<UserInfoViewModel> contacts = new ObservableArrayList<>();
    private String title = "Blocked Users";
    private BlockContactsMvpView mvpView;

    public BlockContactsMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(BlockContactsMvpView mvpView) {
        this.mvpView = mvpView;
    }

    private void generateContactVM(List<Contact> contactList){
        contacts.clear();
        for(Contact contact: contactList){
            UserInfoViewModel<Contact> vm = new UserInfoViewModel<>();
            vm.setData(contact);
            contacts.add(vm);
        }
    }

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener(){
        return new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Object data) {
                UserInfoViewModel<Contact> contact = contacts.get(position);
                if(mvpView!=null) {
                    mvpView.goToProfileFragment(contact.getData());
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

    public void setContactList(List<Contact> list){
        generateContactVM(list);
    }

    public BlockContactsViewModel(ContactPresenter presenter){
        this.presenter = presenter;
    }

    public void loadData(){
        presenter.getBlockList();
    }


//    public SearchBar.OnEditListener getOnEditListener(){
//        return new SearchBar.OnEditListener() {
//            @Override
//            public void onEditing(View view, String text) {
//                if(text.equals("")){
//                    setSearching(false);
//                }else{
//                    setSearching(true);
//                }
//                updateSearchListView(filterData(text));
//            }
//        };
//    }

}

