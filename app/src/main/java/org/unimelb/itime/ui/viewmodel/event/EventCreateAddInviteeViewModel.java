package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddInviteeMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/6/22.
 */

public class EventCreateAddInviteeViewModel extends BaseObservable {
    private EventCreatePresenter presenter;
    private Context context;
    private EventCreateAddInviteeMvpView mvpView;
    private Event event;
    private List<Invitee> invitees = new ArrayList<>();
    private ObservableList<UserInfoViewModel> inviteeItems = new ObservableArrayList<>();
    private ToolbarViewModel toolbarViewModel;
    private boolean canSeeEachOther = true;

    @Bindable
    public boolean isCanSeeEachOther() {
        return event.getInviteeVisibility()==Event.CAN_SEE_EACH_OTHER;
    }

    public void nextEnable(boolean enable){
        toolbarViewModel.setRightEnable(enable);
    }


    public void setCanSeeEachOther(boolean canSeeEachOther) {
        this.canSeeEachOther = canSeeEachOther;
        if(canSeeEachOther){
            event.setInviteeVisibility(Event.CAN_SEE_EACH_OTHER);
        }else{
            event.setInviteeVisibility(Event.CANNOT_SEE_EACH_OTHER);
        }
        notifyPropertyChanged(com.android.databinding.library.baseAdapters.BR.canSeeEachOther);
    }

    public ToolbarViewModel getToolbarViewModel() {
        return toolbarViewModel;
    }

    public void loadData(){
        generateInviteeItems(invitees);
    }

    @Bindable
    public ObservableList<UserInfoViewModel> getInviteeItems() {
        return inviteeItems;
    }

    public void setInviteeItems(ObservableList<UserInfoViewModel> inviteeItems) {
        this.inviteeItems = inviteeItems;
        notifyPropertyChanged(BR.inviteeItems);
    }

    private void generateInviteeItems(List<Invitee> invitees){
        inviteeItems.clear();

        for(Invitee invitee: invitees){
            UserInfoViewModel<Invitee> vm = new UserInfoViewModel<>();
            vm.setData(invitee);
            vm.setSelect(true);
            inviteeItems.add(vm);
        }

        nextEnable(!inviteeItems.isEmpty());
    }


    public OnRecyclerItemClickListener.OnItemClickListener getOnItemClick(){
        return new OnRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInfoViewModel<Invitee> invitee = inviteeItems.get(position);
                if(invitees.contains(invitee.getData())){
                    invitees.remove(invitee.getData());
                    inviteeItems.remove(invitee);
                }
                nextEnable(!inviteeItems.isEmpty());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                onItemClick(view, position);
            }
        };
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(com.android.databinding.library.baseAdapters.BR.viewModel, R.layout.listview_selectable_user_item);
    }

    public void setToolbarViewModel(ToolbarViewModel toolbarViewModel) {
        this.toolbarViewModel = toolbarViewModel;
    }

    public EventCreateAddInviteeViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
    }

    public EventCreatePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(EventCreatePresenter presenter) {
        this.presenter = presenter;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EventCreateAddInviteeMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(EventCreateAddInviteeMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
        event.setInvitees(invitees);
    }

    public View.OnClickListener onSearchClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.gotoSearch();
            }
        };
    }

    public View.OnClickListener onAddClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.gotoAddContact();
            }
        };
    }
}
