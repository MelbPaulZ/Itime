package org.unimelb.itime.ui.fragment.login;


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
import org.unimelb.itime.databinding.FragmentLoginResetPasswordBinding;
import org.unimelb.itime.ui.mvpview.login.LoginResetPasswordMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.login.LoginResetPasswordViewModel;

/**
 * Created by Paul on 30/8/17.
 */

public class LoginResetPasswordFragment extends ItimeBaseFragment<LoginResetPasswordMvpView, LoginPresenter<LoginResetPasswordMvpView>>
        implements LoginResetPasswordMvpView, ToolbarInterface{
    private FragmentLoginResetPasswordBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private LoginResetPasswordViewModel vm;

    @Override
    public LoginPresenter<LoginResetPasswordMvpView> createPresenter() {
        return new LoginPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_reset_password, container,false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setRightText(getString(R.string.reset));
        binding.setToolbarVM(toolbarViewModel);
        vm = new LoginResetPasswordViewModel(getPresenter());
        vm.setOnEmailChangeListener(email -> {
            if (email.length() == 0){
                toolbarViewModel.setRightEnable(false);
            }else {
                toolbarViewModel.setRightEnable(true);
            }
        });
        binding.setVm(vm);
    }

    @Override
    public void onNext() {
        Toast.makeText(getContext(), "todo: reset", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
