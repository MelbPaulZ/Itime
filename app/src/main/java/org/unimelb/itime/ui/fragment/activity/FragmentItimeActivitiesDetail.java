package org.unimelb.itime.ui.fragment.activity;

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
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.databinding.FragmentItimeActivitiesDetailBinding;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesDetailMvpView;
import org.unimelb.itime.ui.presenter.activity.ItimeActivitiesDetailPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ActivityMessageGroupViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ActivityMessageViewModel;
import org.unimelb.itime.ui.viewmodel.activity.ItimeActivitiesDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/7/17.
 */

public class FragmentItimeActivitiesDetail extends ItimeBaseFragment<ItimeActivitiesDetailMvpView, ItimeActivitiesDetailPresenter<ItimeActivitiesDetailMvpView>>
implements ToolbarInterface{
    private FragmentItimeActivitiesDetailBinding binding;
    private ToolbarViewModel toolbarVM;
    private ItimeActivitiesDetailViewModel vm;
    private MessageGroup messageGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_itime_activities_detail, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new ItimeActivitiesDetailViewModel(getPresenter());
        vm.setMessages(getMessages());
        vm.setMessageGroup(messageGroup);
        binding.setVm(vm);

        toolbarVM = new ToolbarViewModel<>(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setRightText(getString(R.string.toolbar_mute));
        toolbarVM.setRightTextColor(getContext().getResources().getColor(R.color.black));
        binding.setToolbarVM(toolbarVM);
    }

    public void setMessageGroup(MessageGroup messageGroup) {
        this.messageGroup = messageGroup;
    }


    private List<ActivityMessageViewModel> getMessages(){
        List<MessageGroup> messageGroups = DBManager.getInstance(getContext()).find(MessageGroup.class, "messageGroupUid", messageGroup.getMessageGroupUid());
        List<Message> messages = messageGroups.get(0).getMessage();
        List<ActivityMessageViewModel> viewModels = new ArrayList<>();
        for (Message message: messages){
            ActivityMessageViewModel msgViewModel = new ActivityMessageViewModel(message);
            msgViewModel.setContext(getContext());
            viewModels.add(msgViewModel);
        }

        return viewModels;
    }


    @Override
    public ItimeActivitiesDetailPresenter<ItimeActivitiesDetailMvpView> createPresenter() {
        return new ItimeActivitiesDetailPresenter<>(getContext());
    }


    @Override
    public void onNext() {
        // mute
        Toast.makeText(getContext(), "Mute", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }
}
