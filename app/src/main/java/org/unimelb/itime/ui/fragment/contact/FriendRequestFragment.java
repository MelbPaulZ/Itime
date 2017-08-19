package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.databinding.FragmentFriendRequestsBinding;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.mvpview.contact.FriendRequestMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.EditContactViewModel;
import org.unimelb.itime.ui.viewmodel.contact.FriendRequestViewModel;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/8/16.
 */

public class FriendRequestFragment  extends ItimeBaseFragment<FriendRequestMvpView, ContactPresenter<FriendRequestMvpView>> implements FriendRequestMvpView, ToolbarInterface {

    private FriendRequestViewModel viewModel;
    private ToolbarViewModel toolbarViewModel;
    private ProfileFragment profileFragment;
    private FragmentFriendRequestsBinding binding;

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new FriendRequestViewModel(presenter);
        viewModel.setMvpView(this);
        binding.setVm(viewModel);
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.friend_request_title));
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_friend_requests, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getRequestFriendListFromServer();
    }

    @Override
    public ContactPresenter<FriendRequestMvpView> createPresenter() {
        return new ContactPresenter<>(getContext());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case ContactPresenter.TASK_REQUEST_LIST:
                viewModel.setRequests((List<FriendRequest>) data);
                List<String> unreadIds = viewModel.getUnreadIds();
                if(!unreadIds.isEmpty()) {
                    String[] array = new String[unreadIds.size()];
                    presenter.setRead(unreadIds.toArray(array));
                }
                break;
            case ContactPresenter.TASK_REQUEST_ACCEPT:
                viewModel.markAdded((String) data);
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }

    @Override
    public void toProfile(String userUid) {
        if (profileFragment==null){
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUserUid(userUid);
        getBaseActivity().openFragment(profileFragment);
    }
}
