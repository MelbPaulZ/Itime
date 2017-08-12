package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentInviteeListBinding;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;
import org.unimelb.itime.ui.fragment.meeting.CusSwipeViewPager;
import org.unimelb.itime.ui.fragment.meeting.PagerAdapterMeeting;
import org.unimelb.itime.ui.presenter.contact.ProfilePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.InviteeListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/8/11.
 */

public class FragmentEventDetailInviteeList extends ItimeBaseFragment {

    private FragmentInviteeListBinding binding;
    private List<Invitee> invitees = new ArrayList<>();
    private InviteeListViewModel viewModel = new InviteeListViewModel();
    private ProfileFragment profileFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invitee_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setVm(viewModel);
        viewModel.setFragment(this);
        viewModel.setInvitees(invitees);
    }

    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
        if(viewModel!=null){
            viewModel.setInvitees(invitees);
        }
    }

    public void toProfile(String userUid){
        if(profileFragment==null){
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUserUid(userUid);
        getBaseActivity().openFragment(profileFragment);
    }
}
