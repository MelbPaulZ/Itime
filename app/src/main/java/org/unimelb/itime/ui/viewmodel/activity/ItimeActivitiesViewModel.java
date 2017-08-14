package org.unimelb.itime.ui.viewmodel.activity;

import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesPresenter;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 3/7/17.
 */

public class ItimeActivitiesViewModel extends ItimeBaseViewModel {

    private ItimeActivitiesPresenter<ItimeActivitiesMvpView> presenter;
    private View rightView;
    private PopupMenu menu;

    private List<ActivityMessageGroupViewModel> messageGroups = new ArrayList<>();
    public final OnItemBind<ActivityMessageGroupViewModel> onItemBind = new OnItemBind<ActivityMessageGroupViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ActivityMessageGroupViewModel item) {
            if (item.getMessageGroup().getType() == MessageGroup.TYPE_EVENT_MESSAGE_GROUP){
                itemBinding.set(BR.messageGroupViewModel, R.layout.itime_activity_meeting);
            }else if (item.getMessageGroup().getType() == MessageGroup.SYSTEM_MESSAGE_GROUP){
                itemBinding.set(BR.messageGroupViewModel, R.layout.itime_activity_system);
            }else{ // // TODO: 4/7/17 change later
                itemBinding.set(BR.messageGroupViewModel, R.layout.itime_activity_meeting);
            }
        }
    };

    public ItimeActivitiesViewModel(ItimeActivitiesPresenter<ItimeActivitiesMvpView> presenter) {
        this.presenter = presenter;
        initPopup();
    }

    @Bindable
    public List<ActivityMessageGroupViewModel> getMessageGroups() {
        return messageGroups;
    }

    public void setMessageGroups(List<ActivityMessageGroupViewModel> messageGroups) {
        this.messageGroups = messageGroups;
        notifyPropertyChanged(BR.messageGroups);
    }

    public void updateMessageGroups(List<MessageGroup> newMessageGroups){
        for (MessageGroup messageGroup: newMessageGroups){
            ActivityMessageGroupViewModel groupViewModel = findViewModelByMessageGroup(messageGroup);
            if (groupViewModel == null){
                // create new message group viewmodel if this is not exist
                groupViewModel = new ActivityMessageGroupViewModel(presenter.getContext(), messageGroup);
                messageGroups.add(0, groupViewModel);
            }else{
                // update message
                groupViewModel.setMessageGroup(messageGroup);
            }
        }
    }


    private ActivityMessageGroupViewModel findViewModelByMessageGroup(MessageGroup messageGroup){
        for (ActivityMessageGroupViewModel viewModel : messageGroups){
            if (viewModel.getMessageGroup().getMessageGroupUid() == messageGroup.getMessageGroupUid()){
                return viewModel;
            }
        }
        return null;
    }

    public void setRightView(View rightView){
        this.rightView = rightView;
    }

    public void onClickRight(){
        menu.showLocation(rightView);
    }



    private void initPopup(){
        menu = new PopupMenu(presenter.getContext());
        List<PopupMenu.Item> menuItem = new ArrayList<>();

        menuItem.add(new PopupMenu.Item(R.drawable.icon_contacts_invitenew,
                presenter.getContext().getResources().getString(R.string.mark_all_read)));


        menu.setItems(menuItem);

        PopupMenu.OnItemClickListener onMenuItemClicked = new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(int position, PopupMenu.Item item) {
                presenter.readAll();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_ITIME_ACTIVITIES));
                Toast.makeText(presenter.getContext(), "All Read", Toast.LENGTH_SHORT).show();
            }
        };
        menu.setOnItemClickListener(onMenuItemClicked);
    }
}
