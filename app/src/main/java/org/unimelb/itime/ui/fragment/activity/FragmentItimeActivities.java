package org.unimelb.itime.ui.fragment.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.databinding.FragmentItimeActivitiesBinding;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.ItimeActivitiesActivity;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ActivityMessageGroupViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ActivityMessageViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ItimeActivitiesViewModel;
import org.unimelb.itime.util.MessageGroupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 3/7/17.
 */

public class FragmentItimeActivities extends ItimeBaseFragment<ItimeActivitiesMvpView, ItimeActivitiesPresenter<ItimeActivitiesMvpView>>
        implements ItimeActivitiesMvpView, ToolbarInterface{
    private FragmentItimeActivitiesBinding binding;
    private ItimeActivitiesViewModel vm;
    private ToolbarViewModel toolbarViewModel;
    private View rightBtn;

    public static boolean needUpdateActivities = false;

    @Override
    public ItimeActivitiesPresenter<ItimeActivitiesMvpView> createPresenter() {
        return new ItimeActivitiesPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_itime_activities, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needUpdateActivities){
            vm.setMessageGroups(getMessageViewGroups());
            needUpdateActivities = false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new ItimeActivitiesViewModel(getPresenter());
        vm.setMessageGroups(getMessageViewGroups());
        rightBtn = binding.getRoot().findViewById(R.id.right_icon);
        vm.setRightView(rightBtn);
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setRightIconVisibility(View.VISIBLE);
        toolbarViewModel.setRightIcon(getContext().getResources().getDrawable(R.drawable.icon_more));
        toolbarViewModel.setTitle(getString(R.string.toolbar_activities));
        binding.setToolbarVM(toolbarViewModel);
    }

    private List<ActivityMessageGroupViewModel> getMessageViewGroups(){
        DBManager dbManager = DBManager.getInstance(getContext());
        List<MessageGroup> messageGroups = dbManager.getAll(MessageGroup.class);
        MessageGroupUtil.sortMessageGroupByTime(messageGroups);
        if (vm.getMessageGroups().size() == 0) {
            List<ActivityMessageGroupViewModel> activityMessageGroupViewModels = new ArrayList<>();
            for (MessageGroup messageGroup : messageGroups) {
                ActivityMessageGroupViewModel msgGroupVM = new ActivityMessageGroupViewModel(getContext(), messageGroup);
                msgGroupVM.setMvpView(this);
                activityMessageGroupViewModels.add(msgGroupVM);
            }
            return activityMessageGroupViewModels;
        }else{
            // just simply update.
            vm.updateMessageGroups(messageGroups);
            return vm.getMessageGroups();
        }
    }

    @Subscribe
    public void updateActivities(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_ITIME_ACTIVITIES){
            vm.setMessageGroups(getMessageViewGroups());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onNext() {
        vm.onClickRight();
    }

    @Override
    public void onBack() {

    }


    private MessageGroup getMessageGroupByUid(int messageGroupUid){
        return DBManager.getInstance(getContext()).find(MessageGroup.class, "messageGroupUid", messageGroupUid).get(0);
    }
    @Override
    public void onClickViewMore(int messageGroupUid) {
        Intent intent = new Intent(getActivity(), ItimeActivitiesActivity.class);
        intent.putExtra(ItimeActivitiesActivity.ACTIVITIES_MEETING, getMessageGroupByUid(messageGroupUid));
        startActivity(intent);
    }

    @Override
    public void onDisplayMessages(MessageGroup messageGroup) {
        presenter.readMessageGroup(messageGroup);
    }

    @Override
    public void onReadMessages(Message message) {
        List<ActivityMessageGroupViewModel> msgGroups = vm.getMessageGroups();
        for (ActivityMessageGroupViewModel viewmodel: msgGroups){
            if (viewmodel.getMessageGroup().getMessageGroupUid() == message.getMessageGroupUid()){
                for (ActivityMessageViewModel msgs: viewmodel.getMessageGroups()){
                    msgs.getMessage().setRead(true);
                }
                break; // TODO: 26/8/17 test this click read 
            }
        }
    }
}
