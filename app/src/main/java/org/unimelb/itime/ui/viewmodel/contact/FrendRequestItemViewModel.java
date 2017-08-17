package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestWrapper;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.widget.listview.UserInfoViewModel;


/**
 * Created by 37925 on 2016/12/9.
 */

public class FrendRequestItemViewModel extends UserInfoViewModel<FriendRequest>{

    private ContactPresenter presenter;
    private View.OnClickListener onClickListener;
    private boolean showNewFriendsLabel = false;
    private boolean showReceivedLabel = false;
    private boolean showSentLable= false;

    @Bindable
    public boolean isShowNewFriendsLabel() {
        return showNewFriendsLabel;
    }

    public void setShowNewFriendsLabel(boolean showNewFriendsLabel) {
        this.showNewFriendsLabel = showNewFriendsLabel;
        notifyPropertyChanged(BR.showNewFriendsLabel);
    }

    @Bindable
    public boolean isShowReceivedLabel() {
        return showReceivedLabel;
    }

    public void setShowReceivedLabel(boolean showReceivedLabel) {
        this.showReceivedLabel = showReceivedLabel;
        notifyPropertyChanged(BR.showReceivedLabel);
    }

    @Bindable
    public boolean isShowSentLable() {
        return showSentLable;
    }

    public void setShowSentLable(boolean showSentLable) {
        this.showSentLable = showSentLable;
        notifyPropertyChanged(BR.showSentLable);
    }

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyPropertyChanged(BR.onClickListener);
    }

    @Bindable
    public String getDisplayStatus() {
        return getData().getDisplayStatus();
    }

    public void setDisplayStatus(String displayStatus) {
        getData().setDisplayStatus(displayStatus);
        notifyPropertyChanged(BR.displayStatus);
    }

    public FrendRequestItemViewModel(ContactPresenter presenter){
        this.presenter = presenter;
    }

//    @Bindable
//    public SpannableString getName(){
//        return changeMatchColor(friendRequestWrapper.getName(), friendRequestWrapper.getMatchStr());
//    }
//
//    @Bindable
//    public SpannableString getContactId(){
//        return changeMatchColor(friendRequestWrapper.getUserId(), friendRequestWrapper.getMatchStr());
//    }

    public boolean getShowDetail() {
        return false;
    }

    public void setShowDetail(boolean showDetail) {

    }
}
