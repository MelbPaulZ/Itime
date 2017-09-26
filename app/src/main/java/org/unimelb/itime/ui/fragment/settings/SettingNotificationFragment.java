package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.databinding.FragmentSettingNotificationsBinding;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.ui.viewmodel.SettingViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingNotificationFragment extends ItimeBaseFragment<EventCreateNewMvpView.SettingNotificationMvpView<Setting>, SettingPresenter<EventCreateNewMvpView.SettingNotificationMvpView<Setting>>> implements ToolbarInterface, EventCreateNewMvpView.SettingNotificationMvpView<Object> {

    private FragmentSettingNotificationsBinding binding;
    private SettingViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_notifications, container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentViewModel = new SettingViewModel(getPresenter());
        contentViewModel.setUser(UserUtil.getInstance(getContext()).copyUser());

        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.notifications));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onResume(){
        super.onResume();
        contentViewModel.setSetting(UserUtil.getInstance(getContext()).copySetting());
    }

    @Override
    public SettingPresenter<EventCreateNewMvpView.SettingNotificationMvpView<Setting>> createPresenter() {
        return new SettingPresenter<>(getContext());
    }

    @Override
    public void onBack() {
        getBaseActivity().finish();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            case SettingPresenter.TASK_TO_SETTING:
                gotoSetting();
                break;
        }
    }

    private void gotoSetting(){
        String packageName = getContext().getPackageName();
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
        startActivity(intent);
    }

    @Override
    public void onClickDefaultAlert() {
        getBaseActivity().openFragment(new SettingStDefaultAlertFragment());
    }
}
