package org.unimelb.itime.ui.fragment.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.databinding.FragmentItimeActivitiesBinding;
import org.unimelb.itime.ui.activity.ItimeActivitiesActivity;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ActivityMessageGroupViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ItimeActivitiesViewModel;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new ItimeActivitiesViewModel(getPresenter());
        vm.setMessageGroups(getMessageViewGroups());
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setRightIconVisibility(View.VISIBLE);
        toolbarViewModel.setRightIcon(getContext().getResources().getDrawable(R.drawable.icon_more));
        toolbarViewModel.setTitle(getString(R.string.toolbar_activities));
        binding.setToolbarVM(toolbarViewModel);
    }

    private List<ActivityMessageGroupViewModel> getMessageViewGroups(){
        List<ActivityMessageGroupViewModel> activityMessageGroupViewModels = new ArrayList<>();
        activityMessageGroupViewModels.add(getMockSystemMessageGroupViewModel());
        activityMessageGroupViewModels.add(getMockEventMessageGroupViewModel());
        activityMessageGroupViewModels.add(getMockEventMessageGroupViewModel());
        activityMessageGroupViewModels.add(getMockSystemMessageGroupViewModel());
        activityMessageGroupViewModels.add(getMockEventMessageGroupViewModel());
        activityMessageGroupViewModels.add(getMockEventMessageGroupViewModel());
        activityMessageGroupViewModels.add(getMockEventMessageGroupViewModel());
        return activityMessageGroupViewModels;
    }

    private ActivityMessageGroupViewModel getMockEventMessageGroupViewModel(){
        ActivityMessageGroupViewModel messageGroupViewModel = new ActivityMessageGroupViewModel(getMockMeetingMessageGroup());
        messageGroupViewModel.setMvpView(this);
        return messageGroupViewModel;
    }

    private ActivityMessageGroupViewModel getMockSystemMessageGroupViewModel(){
        return new ActivityMessageGroupViewModel(getMockSystemMessageGroup());
    }

    private MessageGroup getMockSystemMessageGroup(){
        MessageGroup messageGroup = new MessageGroup();
        messageGroup.setTitle("messageGroupMock");
        messageGroup.setMute(true);
        messageGroup.setType(MessageGroup.SYSTEM_MESSAGE_GROUP);
        return messageGroup;
    }

    private int mockCount = 0;
    private MessageGroup getMockMeetingMessageGroup(){
        MessageGroup messageGroup = new MessageGroup();
        messageGroup.setTitle("messageMeetingGroupMock");
        messageGroup.setMute(mockCount++ % 2 == 0);
        messageGroup.setType(MessageGroup.TYPE_EVENT_MESSAGE_GROUP);
        return messageGroup;
    }




    @Override
    public void onNext() {
        Toast.makeText(getContext(), "more", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBack() {

    }

    // TODO: 4/7/17 implement this when has db
    private MessageGroup getMessageGroupByUid(int messageGroupUid){
        return null;
    }
    @Override
    public void onClickViewMore(int messageGroupUid) {
        Intent intent = new Intent(getActivity(), ItimeActivitiesActivity.class);
        intent.putExtra(ItimeActivitiesActivity.ACTIVITIES_MEETING, getMessageGroupByUid(messageGroupUid));
        startActivity(intent);
    }
}
