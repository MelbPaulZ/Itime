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
import org.unimelb.itime.ui.mvpview.contact.FriendRequestMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.widget.listview.UserInfoViewModel;


/**
 * Created by 37925 on 2016/12/9.
 */

public class FriendRequestItemViewModel extends UserInfoViewModel<FriendRequest>{

    public final static int STATUS_ADDED = 0;
    public final static int STATUS_RECEIVE = 1;
    public final static int STATUS_SENT = 2;

    private int status = 0;
    private ContactPresenter presenter;
    private FriendRequestMvpView mvpView;
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
                if(mvpView!=null){
                    mvpView.toProfile(getData().getUser().getUserUid());
                }
            }
        };
    }

    @Bindable
    public View.OnClickListener getOnAcceptClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter!=null){
                    presenter.acceptRequest(getData().getFreqUid());
                }
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

    public FriendRequestItemViewModel(ContactPresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public SpannableString getSecondInfo() {
        String s = "";
        if(getData().getCommonContact()!=0){
            s = String.format(presenter.getContext().getString(R.string.contact_mutual_friends), getData().getCommonContact());
            return new SpannableString(s);
        }else{
            return super.getSecondInfo();
        }
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    public FriendRequestMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(FriendRequestMvpView mvpView) {
        this.mvpView = mvpView;
    }
}
