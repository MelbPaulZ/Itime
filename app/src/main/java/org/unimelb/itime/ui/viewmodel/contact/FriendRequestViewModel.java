package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.view.menu.MenuView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.mvpview.contact.FriendRequestMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/8/17.
 */

public class FriendRequestViewModel extends BaseObservable {
    private FriendRequestMvpView mvpView;
    private ContactPresenter presenter;
    private ObservableList<FrendRequestItemViewModel> items = new ObservableArrayList<>();

    @Bindable
    public ObservableList<FrendRequestItemViewModel> getItems() {
        return items;
    }

    public void setItems(ObservableList<FrendRequestItemViewModel> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.viewModel, R.layout.listview_friend_request_item);
    }
}
