package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.Bindable;
import android.text.SpannableString;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.RecomandContact;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

/**
 * Created by Qiushuo Huang on 2017/8/17.
 */

public class RecommendContactItemViewModel extends UserInfoViewModel<RecomandContact> {
    private ContactPresenter presenter;
    private AddFriendsMvpView mvpView;
    private boolean sent=false;

    @Bindable
    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
        notifyPropertyChanged(BR.sent);
    }

    public View.OnClickListener onAddClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter!=null)
                    presenter.addUser(getData().getUserUid());
            }
        };
    }

    public View.OnClickListener onInfoClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null)
                    mvpView.goToProfileFragment(getData().getUserUid());
            }
        };
    }

    public ContactPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(ContactPresenter presenter) {
        this.presenter = presenter;
    }

    public AddFriendsMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(AddFriendsMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    @Bindable
    public SpannableString getSecondInfo() {
        return new SpannableString(
                String.format(presenter.getContext().getString(R.string.contact_mutual_friends), getData().getCommonContact()));
    }
}
