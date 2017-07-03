package org.unimelb.itime.ui.viewmodel.activity;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesPresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 3/7/17.
 */

public class ItimeActivitiesViewModel extends ItimeBaseViewModel {

    private ItimeActivitiesPresenter<ItimeActivitiesMvpView> presenter;

    private List<ActivityMessageGroupViewModel> messageGroups = new ArrayList<>();
    public final OnItemBind<ActivityMessageGroupViewModel> onItemBind = new OnItemBind<ActivityMessageGroupViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ActivityMessageGroupViewModel item) {
            itemBinding.set(BR.messageGroupViewModel, item.isSystem()? R.layout.itime_activity_system : R.layout.itime_activity_meeting);
        }
    };

    public ItimeActivitiesViewModel(ItimeActivitiesPresenter<ItimeActivitiesMvpView> presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public List<ActivityMessageGroupViewModel> getMessageGroups() {
        return messageGroups;
    }

    public void setMessageGroups(List<ActivityMessageGroupViewModel> messageGroups) {
        this.messageGroups = messageGroups;
        notifyPropertyChanged(BR.messageGroups);
    }
}
