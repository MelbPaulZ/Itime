package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingMyProfileNameBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.util.UserValidator;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileNameFragment extends ItimeBaseFragment<TaskBasedMvpView<User>, UserPresenter<TaskBasedMvpView<User>>> implements TaskBasedMvpView<User>, ItimeBaseMvpView, ToolbarInterface {

    private FragmentSettingMyProfileNameBinding binding;

    private UserProfileViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;
    private UserPresenter<TaskBasedMvpView<User>> presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile_name, container, false);
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
        contentViewModel.setUser(user.clone());

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.name));
        toolbarViewModel.setRightText(getString(R.string.setting_save));

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
        if(UserValidator.getInstance(getContext()).isValidName(contentViewModel.getUser().getPersonalAlias())) {
            contentViewModel.onEditNameDoneClick().onClick(null);
        } else{
            showDialog(getString(R.string.edit_name_alert_msg), null);
        }
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, User data) {
        hideProgressDialog();
        getBaseActivity().openFragment(new SettingMyProfileFragment());
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }
}
