package org.unimelb.itime.ui.viewmodel.activity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;

/**
 * Created by Paul on 4/7/17.
 */

public class ActivityMessageViewModel extends BaseObservable{
    private Message message;
    private ItimeActivitiesMvpView mvpView;

    public ActivityMessageViewModel(Message message) {
        this.message = message;
    }

    public void setMvpView(ItimeActivitiesMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Bindable
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public void onClickViewMore(){
        if (mvpView!=null){
            mvpView.onClickViewMore(message.getMessageGroupUid());
        }
    }
}