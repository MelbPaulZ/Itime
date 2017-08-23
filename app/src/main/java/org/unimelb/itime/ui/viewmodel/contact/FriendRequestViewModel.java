package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.ui.mvpview.contact.FriendRequestMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/8/17.
 */

public class FriendRequestViewModel extends BaseObservable {
    private FriendRequestMvpView mvpView;
    private ContactPresenter presenter;
    private ObservableList<FriendRequestItemViewModel> items = new ObservableArrayList<>();
    private List<FriendRequest> requests = new ArrayList<>();
    private List<String> unreadIds = new ArrayList<>();

    public FriendRequestViewModel(ContactPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public ObservableList<FriendRequestItemViewModel> getItems() {
        return items;
    }

    public void setItems(ObservableList<FriendRequestItemViewModel> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.viewModel, R.layout.listview_friend_request_item);
    }

    public FriendRequestMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(FriendRequestMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public List<FriendRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<FriendRequest> requests) {
        this.requests = requests;
        generateItems();
    }

    /**
     * new = sender=false, status = confirmed, read=false
     * recNum = sender=false, status = confirmed | sent
     * sentNum = sender = true, status = sent
     */
    private void generateItems(){
        List<FriendRequestItemViewModel> newRequsts = new ArrayList<>();
        List<FriendRequestItemViewModel> recRequsts = new ArrayList<>();
        List<FriendRequestItemViewModel> sentRequsts = new ArrayList<>();
        items.clear();
        unreadIds.clear();

        for(FriendRequest request:requests){
            FriendRequestItemViewModel itemViewModel = new FriendRequestItemViewModel(presenter);
            itemViewModel.setData(request);
            itemViewModel.setMvpView(mvpView);

            if(!request.isSender() && !request.receiverIsRead() &&
                    request.getStatus().equals(FriendRequest.STATUS_CONFIRMED)){
                newRequsts.add(itemViewModel);
                itemViewModel.setStatus(FriendRequestItemViewModel.STATUS_ADDED);

            }else if(request.isSender() &&
                    request.getStatus().equals(FriendRequest.STATUS_SENT)){
                sentRequsts.add(itemViewModel);
                itemViewModel.setStatus(FriendRequestItemViewModel.STATUS_SENT);

            }else if(!request.isSender() && request.getStatus().equals(FriendRequest.STATUS_SENT)){
                recRequsts.add(itemViewModel);
                itemViewModel.setStatus(FriendRequestItemViewModel.STATUS_RECEIVE);
            }
        }

        if(!newRequsts.isEmpty()){
            newRequsts.get(0).setShowNewFriendsLabel(true);
        }
        if(!recRequsts.isEmpty()) {
            recRequsts.get(0).setShowReceivedLabel(true);
        }
        if(!sentRequsts.isEmpty()){
            sentRequsts.get(0).setShowSentLable(true);
        }

        items.addAll(newRequsts);
        items.addAll(recRequsts);
        items.addAll(sentRequsts);
    }

    public List<String> getUnreadIds() {
        return unreadIds;
    }

    public void setUnreadIds(List<String> unreadIds) {
        this.unreadIds = unreadIds;
    }

    public void markAdded(String requestUid){
        for(FriendRequestItemViewModel item:items){
            if(item.getData().getFreqUid().equals(requestUid)){
                item.setStatus(FriendRequestItemViewModel.STATUS_ADDED);
                break;
            }
        }
    }


}
