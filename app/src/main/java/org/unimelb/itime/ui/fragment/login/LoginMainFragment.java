package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.databinding.FragmentLoginMainBinding;
import org.unimelb.itime.ui.mvpview.login.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.login.LoginMainViewModel;

/**
 * Created by Paul on 29/8/17.
 */

public class LoginMainFragment extends ItimeBaseFragment<LoginMvpView, LoginPresenter<LoginMvpView>> implements LoginMvpView, ToolbarInterface{
    private FragmentLoginMainBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private LoginMainViewModel vm;

    @Override
    public LoginPresenter<LoginMvpView> createPresenter() {
        return new LoginPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_main, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarViewModel = new ToolbarViewModel(this);
        binding.setToolbarVM(toolbarViewModel);

        vm = new LoginMainViewModel(getPresenter());
        binding.setVm(vm);

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

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
