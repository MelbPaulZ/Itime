package org.unimelb.itime.ui.viewmodel.activity;

import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesDetailPresenter;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesPresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 4/7/17.
 */

public class ItimeActivitiesDetailViewModel extends ItimeBaseViewModel {

    private ItimeActivitiesDetailPresenter presenter;
    private MessageGroup messageGroup;
    private List<ActivityMessageViewModel> messages = new ArrayList<>();
    public final OnItemBind<ActivityMessageViewModel> onItemBind = new OnItemBind<ActivityMessageViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ActivityMessageViewModel item) {
            itemBinding.set(BR.messageViewGroup, R.layout.itime_activity_meeting_message);
        }
    };

    public ItimeActivitiesDetailViewModel(ItimeActivitiesDetailPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public MessageGroup getMessageGroup() {
        return messageGroup;
    }

    public void setMessageGroup(MessageGroup messageGroup) {
        this.messageGroup = messageGroup;
        notifyPropertyChanged(BR.messageGroup);
    }

    @Bindable
    public List<ActivityMessageViewModel> getMessages() {
        return messages;
    }

    public void setMessages(List<ActivityMessageViewModel> messages) {
        this.messages = messages;
        notifyPropertyChanged(BR.messages);
    }

}
