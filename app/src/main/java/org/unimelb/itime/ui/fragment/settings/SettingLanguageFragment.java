package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.databinding.FragmentSettingLanguageBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.setting.SettingLanguageViewModel;

/**
 * Created by Paul on 18/3/17.
 */

public class SettingLanguageFragment extends ItimeBaseFragment<TaskBasedMvpView<Setting>, SettingPresenter<TaskBasedMvpView<Setting>>>
    implements TaskBasedMvpView<Setting>, ToolbarInterface{
    private FragmentSettingLanguageBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private SettingLanguageViewModel contentVM;

    @Override
    public SettingPresenter<TaskBasedMvpView<Setting>> createPresenter() {
        return new SettingPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_language, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_close));
        toolbarViewModel.setTitle(getString(R.string.setting_language));
        toolbarViewModel.setRightText(getString(R.string.setting_save));
        toolbarViewModel.setRightEnable(true);

        contentVM = new SettingLanguageViewModel(getPresenter());
        binding.setToolbarVM(toolbarViewModel);
        binding.setContentVM(contentVM);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Setting data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onBack() {
        getActivity().finish();
    }

    @Override
    public void onNext() {
        showToast("todo: change to another language");
        getActivity().finish();
    }
}
