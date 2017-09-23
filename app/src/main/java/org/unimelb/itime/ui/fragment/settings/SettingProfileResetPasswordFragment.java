package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingResetPassowrdBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingProfileResetPasswordFragment extends ItimeBaseFragment<TaskBasedMvpView<User>, UserPresenter<TaskBasedMvpView<User>>> implements ToolbarInterface, TaskBasedMvpView<User>, ItimeBaseMvpView {

    private FragmentSettingResetPassowrdBinding binding;
    private UserProfileViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;
    private UserPresenter<TaskBasedMvpView<User>> presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_reset_passowrd, container, false);
        return binding.getRoot();
    }

    @Override
    public UserPresenter<TaskBasedMvpView<User>> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = getPresenter();

        contentViewModel = new UserProfileViewModel(presenter);
        User user = UserUtil.getInstance(getContext()).copyUser();
        contentViewModel.setUser(user);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.setting_reset_password));
        toolbarViewModel.setRightText(getString(R.string.done));
        toolbarViewModel.setRightEnable(false);

        contentViewModel.setToolbarViewModel(toolbarViewModel);
        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onBack() {
        getBaseActivity().backFragment(new SettingMyProfileFragment());
    }

    @Override
    public void onNext() {
        contentViewModel.onResetPSWDoneClick().onClick(null);
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, User data) {
        hideProgressDialog();
        switch (taskId){
            case UserPresenter.TASK_USER_UPDATE:{
                showToast(getString(R.string.reset_password_success));
                onBack();
                break;
            }
            default:
                break;
        }
    }


    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case UserPresenter.TASK_USER_PSW_NOT_MATCH:
                showDialog(getString(R.string.password_do_not_match), "");
                break;
            case UserPresenter.TASK_PSW_WRONG_LENGTH:
                showDialog(getString(R.string.password_wrong_length), "");
                break;
            case UserPresenter.TASK_NETWORK_FAILED:
                showDialog(getString(R.string.access_fail), "");
                break;
            default:
                break;
        }

    }
}
