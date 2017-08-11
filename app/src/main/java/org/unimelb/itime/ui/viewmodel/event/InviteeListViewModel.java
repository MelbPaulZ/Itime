package org.unimelb.itime.ui.viewmodel.event;

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
import org.unimelb.itime.ui.fragment.event.FragmentEventDetailInviteeList;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/8/11.
 */

public class InviteeListViewModel extends BaseObservable {
    private ObservableList<UserInfoViewModel> items = new ObservableArrayList<>();
    private List<Invitee> invitees = new ArrayList<>();
    private FragmentEventDetailInviteeList fragment;

    public void setInvitees(List<Invitee> invitees){
        items.clear();
        this.invitees = invitees;
        for(Invitee invitee: invitees){
            UserInfoViewModel<Invitee> vm = new UserInfoViewModel<>();
            vm.setData(invitee);
            items.add(vm);
        }
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.viewModel, R.layout.listview_user_item);
    }

    public FragmentEventDetailInviteeList getFragment() {
        return fragment;
    }

    public void setFragment(FragmentEventDetailInviteeList fragment) {
        this.fragment = fragment;
    }

    public AdapterView.OnItemClickListener onItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Invitee invitee = invitees.get(i);
                if(invitee.getUserStatus().equals(Invitee.STATUS_ACCEPTED)) {
                    if (fragment != null) {
                        fragment.toProfile(invitee.getUserUid());
                    }
                }
            }
        };
    }

    @Bindable
    public ObservableList<UserInfoViewModel> getItems() {
        return items;
    }

    public void setItems(ObservableList<UserInfoViewModel> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }
}
